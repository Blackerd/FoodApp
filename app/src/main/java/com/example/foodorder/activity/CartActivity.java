package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.databinding.ActivityCartBinding;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.helper.ManagmentCart;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class CartActivity extends HomeActivity {
    ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    private ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateTotal();
        initCart();

        binding.checkOutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);

            // Chuyển dữ liệu giỏ hàng sang OrderActivity
            ArrayList<Foods> cartItems = managmentCart.getListCart();
            double totalFee = managmentCart.getTotalFee();
            double percentTax = 0.02;
            double deliveryFee = 10;
            double tax = Math.round(totalFee * percentTax * 100) / 100;
            double totalAmount = Math.round((totalFee + tax + deliveryFee) * 100) / 100;

            intent.putExtra("cartItems", cartItems);
            intent.putExtra("totalAmount", totalAmount);
            intent.putExtra("itemTotal", totalFee);
            intent.putExtra("deliveryFee", deliveryFee);
            intent.putExtra("tax", tax);

            startActivity(intent);
        });
    }

    private void initCart() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, () -> calculateTotal()));
    }

    private void calculateTotal() {
        double percentTax = 0.02;
        double deliveryFee = 10;
        double tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + deliveryFee) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFreeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + deliveryFee);
        binding.totalTxt.setText("$" + total);


    }

    private void setVariable() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        bottomNavigationView.setItemSelected(R.id.cart, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            } else if (id == R.id.cart) {
                startActivity(new Intent(CartActivity.this, CartActivity.class));
            }
        });
    }
}
