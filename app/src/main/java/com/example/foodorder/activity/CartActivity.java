package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.bag.BagItemHolderRecycle;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.databinding.ActivityCartBinding;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.helper.ManagmentCart;
import com.example.foodorder.model.Food;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartActivity extends HomeActivity {
    ActivityCartBinding binding;
    private TextView totalFreeTxt, totalTxt;
    private int totalPurchase = 0;
    private ChipNavigationBar bottomNavigationView;
    private RecyclerView recyclerView;
    private BagItemHolderRecycle bagItemHolderRecycle;
    List<Food> listPurchase = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(v -> finish());
        this.totalFreeTxt = findViewById(R.id.totalFreeTxt);
        this.totalFreeTxt.setText(0 + " .000 VND");
        this.totalTxt = findViewById(R.id.totalTxt);
        setVariable();
        Bundle bundle
                = getIntent().getBundleExtra("bundle");
        this.listPurchase = (List<Food>) bundle.getSerializable("listDataPurchase");
//        checkIfEmpty();
        bagItemHolderRecycle = new BagItemHolderRecycle(this.listPurchase);
        bagItemHolderRecycle.setOnBTNClick(new BagItemHolderRecycle.OnBTNClick() {
            @Override
            public void increment(int potiton, TextView view) {
                Food food = listPurchase.get(potiton);
                food.quantity += 1;
                view.setText("" + food.quantity);
                totalPurchase = (int) (totalPurchase + food.price);
                totalFreeTxt.setText(totalPurchase + " .000 VND");
                totalTxt.setText((totalPurchase + 30 + 0) + " .000 VND");

            }

            @Override
            public void decrement(int position, TextView view) {
                Food food = listPurchase.get(position);
                food.quantity -= 1;
                view.setText("" + food.quantity);
                totalPurchase = (int) (totalPurchase - food.price);
                totalFreeTxt.setText(totalPurchase + " .000 VND");
                totalTxt.setText((totalPurchase + 30 + 0) + " .000 VND");
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.show_item_home_bag);
        recyclerView.setAdapter(bagItemHolderRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        binding.purchase.setOnClickListener(v -> {
            List<Food> itemChoosed = listPurchase.stream().filter(f -> f.quantity > 0).collect(Collectors.toList());
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            bundle.putSerializable("itemChooseds", (Serializable) itemChoosed);
            bundle.putInt("itemprice", totalPurchase);
            bundle.putInt("total", totalPurchase);
            intent.putExtra("data", bundle);
            startActivity(intent);
        });
    }

    private void checkIfEmpty() {
        if (listPurchase.isEmpty() || listPurchase == null) {
            recyclerView.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            binding.emptyView.setVisibility(View.GONE);
        }
    }
//    private void initCart() {
//        if (managmentCart.getListCart().isEmpty()) {
//            binding.emptyTxt.setVisibility(View.VISIBLE);
//            binding.scrViewCart.setVisibility(View.GONE);
//        } else {
//            binding.emptyTxt.setVisibility(View.GONE);
//            binding.scrViewCart.setVisibility(View.VISIBLE);
//        }
//        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, () -> calculateTotal()));
//    }

//    private void calculateTotal() {
//        double percentTax = 0.02;
//        double deliveryFee = 10;
//        double tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100;
//        double total = Math.round((managmentCart.getTotalFee() + tax + deliveryFee) * 100) / 100;
//        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
//
//        binding.totalFreeTxt.setText("$" + itemTotal);
//        binding.taxTxt.setText("$" + tax);
//        binding.deliveryTxt.setText("$" + deliveryFee);
//        binding.totalTxt.setText("$" + total);
//
//
//    }

    private void setVariable() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        bottomNavigationView.setItemSelected(R.id.cart, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listDataPurchase", (Serializable) listPurchase);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else if (id == R.id.cart) {
                startActivity(new Intent(CartActivity.this, CartActivity.class));
            } else if (id == R.id.profile) {
                startActivity(new Intent(CartActivity.this, ProfileActivity.class));
            } else if (id == R.id.orderHistory) {
                startActivity(new Intent(CartActivity.this, OrdersHistoryActivity.class));
            }
        });
    }
}
