package com.example.foodorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.FoodListAdapter;
import com.example.foodorder.databinding.ActivityListFoodBinding;
import com.example.foodorder.domain.Foods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends HomeActivity {
    private ActivityListFoodBinding binding;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
    }

    private void initList() {
        DatabaseReference foodRef = database.getReference("Foods");
        binding.progressBarListFood.setVisibility(View.VISIBLE); // Hiện ProgressBar
        ArrayList<Foods> foods = new ArrayList<>();
        Query query = foodRef.orderByChild("CategoryId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Firebase", "Data snapshot: " + snapshot.toString()); // Log dữ liệu snapshot
                binding.progressBarListFood.setVisibility(View.GONE); // Ẩn ProgressBar

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Foods food = dataSnapshot.getValue(Foods.class);
                        if (food != null) {
                            // Kiểm tra từng thuộc tính của Foods
                            if (food.getTitle() == null) {
                                Log.e("ListFoodActivity", "Title is null for food: " + food.getId());
                                continue; // Bỏ qua nếu title là null
                            }
                            foods.add(food);
                        } else {
                            Log.e("ListFoodActivity", "Food is null at position: " + dataSnapshot.getKey());
                        }
                    }
                    if (foods.size() > 0) {
                        binding.foodListView.setLayoutManager(new LinearLayoutManager(ListFoodActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.foodListView.setAdapter(new FoodListAdapter(foods));
                    } else {
                        // Show a message if no foods are available
                        Log.d("Firebase", "No foods found for this category");
                    }
                } else {
                    // Handle the case when no data is returned
                    Log.d("Firebase", "No data snapshot exists");
                }
                binding.progressBarListFood.setVisibility(View.GONE); // Ẩn ProgressBar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage()); // Log lỗi
                binding.progressBarListFood.setVisibility(View.GONE); // Ẩn ProgressBar
            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");

        Log.d("IntentExtra", "CategoryId: " + categoryId + ", CategoryName: " + categoryName); // Log thông tin Intent

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
