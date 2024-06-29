package com.example.foodorder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CategoryAdapter;
import com.example.foodorder.adapter.FoodListAdapter;
import com.example.foodorder.adapter.SliderAdapter;
import com.example.foodorder.databinding.ActivityMainBinding;
import com.example.foodorder.domain.Category;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.domain.SliderItems;
import com.example.foodorder.domain.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    DatabaseReference database;
    private ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Hiển thị tên người dùng lên TextView
        TextView userNameTextView = findViewById(R.id.userNameTv);
        userNameTextView.setText(getUserNameFromPrefs());

        initCategory();
        setVariables();
        initBanner();
        initInfoUser();
        setupSearchFunctionality();
    }
    // Phương thức để lấy tên người dùng từ SharedPreferences
    private String getUserNameFromPrefs() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("user_name", "Tên người dùng");
    }

    private void initInfoUser() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = database.child("Users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Users user = snapshot.getValue(Users.class);
                        if (user != null) {
                            binding.userNameTv.setText(user.getName());
                        } else {
                            Log.e("User", "User is null for snapshot: " + snapshot.toString());
                        }
                    } else {
                        Log.e("User", "Snapshot does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("User", "Database error: " + error.getMessage());
                }
            });
        }
    }


    private void initBanner() {
        DatabaseReference bannerRef = database.child("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        bannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SliderItems sliderItems = dataSnapshot.getValue(SliderItems.class);
                        if (sliderItems != null) {
                            items.add(sliderItems);
                            binding.progressBarBanner.setVisibility(View.GONE);
                            Log.d("Banner", "Image: " + sliderItems.getImage());
                        } else {
                            Log.e("Banner", "SliderItems is null for snapshot: " + dataSnapshot.toString());
                        }
                    }
                    if (items.size() > 0) {
                        banners(items);
                    }
                } else {
                    Log.e("Banner", "Snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Banner", "Database error: " + error.getMessage());
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpager2.setAdapter(new SliderAdapter(items, binding.viewpager2));
        binding.viewpager2.setClipToPadding(false);
        binding.viewpager2.setClipChildren(false);
        binding.viewpager2.setOffscreenPageLimit(3);
        binding.viewpager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        bottomNavigationView.setItemSelected(R.id.home, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            } else if (id == R.id.cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        // Xử lý khi nhấn vào ImageView (đăng xuất)
        binding.userImage.setOnClickListener(v -> {
            if (binding.logoutLayout.getVisibility() == View.VISIBLE) {
                binding.logoutLayout.setVisibility(View.GONE);
            } else {
                binding.logoutLayout.setVisibility(View.VISIBLE);
            }
        });

        // ẩn layout logout khi click vào nút close
        binding.logoutButton.setOnClickListener(v -> {
            logoutUser();
        });
    }

    // đăng xuất người dùng
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // xóa thông tin người dùng trong SharedPreferences
        clearUserInfo();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // xóa thông tin người dùng trong SharedPreferences
    private void clearUserInfo() {
        // lấy SharedPreferences và xóa thông tin người dùng
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    private void initCategory() {
        DatabaseReference categoryRef = database.child("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> categories = new ArrayList<>();

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categories.add(dataSnapshot.getValue(Category.class));
                    }
                    if (categories.size() > 0) {
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                        binding.categoryView.setAdapter(new CategoryAdapter(categories));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                } else {
                    Log.e("Category", "Snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Category", "Database error: " + error.getMessage());
            }
        });
    }

    private void setupSearchFunctionality() {
        // Xử lý tìm kiếm sản phẩm
        binding.searchBtn.setOnClickListener(v -> {
            String searchText = binding.edtSearch.getText().toString().trim();
            if (!searchText.isEmpty()) {
                filterProducts(searchText);
            } else {
                // Nếu không có văn bản tìm kiếm, hiển thị danh sách ban đầu
                initCategory(); // Hoặc load lại dữ liệu ban đầu
            }
        });
    }

    private void filterProducts(String searchText) {
        DatabaseReference productsRef = database.child("Products");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Foods> filteredProducts = new ArrayList<>();

        productsRef.orderByChild("title")
                .startAt(searchText.toLowerCase())
                .endAt(searchText.toLowerCase() + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Foods product = dataSnapshot.getValue(Foods.class);
                                if (product != null) {
                                    filteredProducts.add(product);
                                }
                            }
                            if (filteredProducts.size() > 0) {
                                binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                                // Hiển thị danh sách sản phẩm đã lọc
                                binding.categoryView.setAdapter(new FoodListAdapter(filteredProducts));
                            } else {
                                // Hiển thị thông báo không tìm thấy sản phẩm
                                Log.d("Search", "No products found with keyword: " + searchText);
                            }
                        } else {
                            // Hiển thị thông báo không tìm thấy sản phẩm
                            Log.d("Search", "No products found with keyword: " + searchText);
                        }
                        binding.progressBarCategory.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Search", "Database error: " + error.getMessage());
                        binding.progressBarCategory.setVisibility(View.GONE);
                    }
                });
    }

}
