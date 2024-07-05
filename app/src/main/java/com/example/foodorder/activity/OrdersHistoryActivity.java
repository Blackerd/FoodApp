package com.example.foodorder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.adapter.OrderHistoryAdapter;
import com.example.foodorder.databinding.ActivityOrdersHistoryBinding;
import com.example.foodorder.domain.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrdersHistoryActivity extends AppCompatActivity {

    private ActivityOrdersHistoryBinding binding;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<Orders> ordersList;
    private ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariables();
        setupRecyclerView();

        // Retrieve logged-in user's ID from SharedPreferences
        int userId = getUserIDFromSharedPreferences();

        // Fetch user's order history from Firebase Database
        fetchUserOrderHistory(userId);
    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        bottomNavigationView.setItemSelected(R.id.orderHistory, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(OrdersHistoryActivity.this, MainActivity.class));
            } else if (id == R.id.cart) {
                Intent intent = new Intent(OrdersHistoryActivity.this, CartActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else if (id == R.id.profile) {
                startActivity(new Intent(OrdersHistoryActivity.this, ProfileActivity.class));
            } else if (id == R.id.orderHistory) {
                startActivity(new Intent(OrdersHistoryActivity.this, OrdersHistoryActivity.class));
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = binding.ordersHistoryView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, ordersList); // Pass adapter with list
        recyclerView.setAdapter(adapter);
    }

    private void fetchUserOrderHistory(int userId) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        Query userOrdersQuery = ordersRef.orderByChild("userId").equalTo(userId);
        userOrdersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders order = snapshot.getValue(Orders.class);
                    if (order != null) {
                        ordersList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private int getUserIDFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getInt("user_id", -1); // -1 as default value if not found
    }
}
