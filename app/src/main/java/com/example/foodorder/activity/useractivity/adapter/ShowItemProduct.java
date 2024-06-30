package com.example.foodorder.activity.useractivity.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.CartActivity;
import com.example.foodorder.activity.MainActivity;
import com.example.foodorder.activity.OrdersHistoryActivity;
import com.example.foodorder.activity.ProfileActivity;
import com.example.foodorder.activity.useractivity.adapter.home.HomeHolderRecycleAdapter;
import com.example.foodorder.activity.useractivity.adapter.item.ShowItemRecycleAdapter;
import com.example.foodorder.model.Food;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowItemProduct extends AppCompatActivity {
    private ShowItemRecycleAdapter showItemRecycleAdapter;
    private RecyclerView showItemRecycle;
    private ImageView showItemImage , bankBTN;
    //    private User userCookie;
    private Button addTocart;
    private ChipNavigationBar bottomNavigationView;
    private TextView showItemName, showItemPrice, showItemType, userName;
    private List<Food> listFood = new ArrayList<>();
    private List<Food> listFoodPurchase = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_product);

//        Khai bao
        this.userName = findViewById(R.id.textNameDetail);
        this.showItemImage = findViewById(R.id.product_image);
        this.showItemName = findViewById(R.id.product_name);
        this.showItemPrice = findViewById(R.id.product_price);
        this.showItemType = findViewById(R.id.product_category);
        this.addTocart = findViewById(R.id.addTocart);
        this.bankBTN = findViewById(R.id.backBtn);
        this.bankBTN.setOnClickListener(v -> finish());
//Bundle
        Bundle bundle = getIntent().getBundleExtra("bundle");
//        this.userCookie = (User) bundle.getSerializable("user");
//        this.userName.setText(userCookie.getUserName());
        this.getItemFoodFromBundleAndSetForShowLayout(bundle);
        this.setViewForAdapter();

        this.addTocart.setOnClickListener(v -> {
            boolean check = listFoodPurchase.stream().anyMatch(food -> food.getName().equals(this.listFood.get(bundle.getInt("position")).getName()));
            if (!check) {
                this.listFoodPurchase.add(this.listFood.get(bundle.getInt("position")));
            }
        });

        setVariables();
    }

    public void setViewForAdapter() {
        this.showItemRecycleAdapter = new ShowItemRecycleAdapter(this.listFood);
        this.showItemRecycleAdapter.setOnClick(new HomeHolderRecycleAdapter.OnDataRecycleAdapter() {
            @Override
            public void onClickBTN(int potision) {

            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ShowItemProduct.this, ShowItemProduct.class);
                addIntentIntoItem(listFood, position, intent);
                startActivity(intent);
            }
        });
        this.showItemRecycle = (RecyclerView) findViewById(R.id.show_other_item_recycle);

        this.showItemRecycle.setAdapter(this.showItemRecycleAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.showItemRecycle.setLayoutManager(layoutManager);
    }

    //    function to process bundle
    protected void getItemFoodFromBundleAndSetForShowLayout(Bundle bundle) {
        int postionLastHomeClick = bundle.getInt("position");
        this.listFood = (List<Food>) bundle.getSerializable("listData");
        this.listFoodPurchase = (List<Food>) bundle.getSerializable("listDataPurchase");
        Food product = (Food) this.listFood.get(postionLastHomeClick);
        if (product != null) {
            this.setViewForShowLayout(product);
        } else {
            Toast.makeText(ShowItemProduct.this, "Null", Toast.LENGTH_LONG).show();
        }

    }

    public void setViewForShowLayout(Food product) {
        String name = product.name.trim();
        Double price = product.price;
        String type = product.type.trim();


        Picasso.get().load(product.getUrl()).into(showItemImage);
        this.showItemName.setText(name);
        this.showItemPrice.setText("Giá:" + Math.round(price) + ".000 VND");
        this.showItemType.setText("Thể Loại: " + type);
    }

    protected void addIntentIntoItem(List<Food> listData, int position, Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
//        bundle.putSerializable("user", this.userCookie);
        bundle.putSerializable("listData", (Serializable) listData);
        intent.putExtra("bundle", bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exits);
    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        // xử lý sự kiện khi người dùng chọn mục khác nhau
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
//                startActivity(new Intent(ShowItemProduct.this, MainActivity.class));
                finish();
            } else if (id == R.id.cart) {
                Intent intent = new Intent(ShowItemProduct.this, CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listDataPurchase", (Serializable) listFoodPurchase);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                startActivity(new Intent(ShowItemProduct.this, CartActivity.class));
            } else if (id == R.id.profile) {
                startActivity(new Intent(ShowItemProduct.this, ProfileActivity.class));
            } else if (id == R.id.orderHistory) {
                startActivity(new Intent(ShowItemProduct.this, OrdersHistoryActivity.class));
            }
        });
    }
}