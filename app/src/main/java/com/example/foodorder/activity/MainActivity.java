package com.example.foodorder.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.AdapterItemAction;
import com.example.foodorder.activity.useractivity.adapter.ShowItemProduct;
import com.example.foodorder.activity.useractivity.adapter.homever.HomeVerRecycle;
import com.example.foodorder.adapter.CategoryAdapter;
import com.example.foodorder.adapter.FoodListAdapter;
import com.example.foodorder.adapter.SliderAdapter;
import com.example.foodorder.databinding.ActivityMainBinding;
import com.example.foodorder.domain.Category;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.domain.SliderItems;
import com.example.foodorder.domain.Users;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.retrofit.food.FoodServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    DatabaseReference database;
    private ChipNavigationBar bottomNavigationView;
    private List<Food> arrProducts, listPurchase = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private HomeVerRecycle homeVerRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Hiển thị tên người dùng lên TextView
        TextView userNameTextView = findViewById(R.id.textName);
        userNameTextView.setText(getUserNameFromPrefs());
        getAllFoodProducts();
//        initCategory();
        setVariables();
//        initBanner();
//        initInfoUser();
//        setupSearchFunctionality();

        this.recyclerView = binding.verRecycle;
        binding.food.setOnClickListener(v -> homeVerRecycle.filterByType("Food"));
        binding.drink.setOnClickListener(v -> homeVerRecycle.filterByType("Drink"));
        binding.fried.setOnClickListener(v -> homeVerRecycle.filterByType("Fried"));
        this.searchView = binding.searchView;
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeVerRecycle.filterByNewtext(newText);
                return false;
            }
        });
    }

    public void getAllFoodProducts() {
        FoodServices foodServices = new FoodServices();
        foodServices.getFood(new GetOnDataListener() {
            @Override
            public void onSuccess(Object o) {
                List<Food> list = (List<Food>) o;
                setHomeVerRecycleOnList(list);
            }

            @Override
            public void onFailure(Object o) {

            }
        });
    }

    public void setHomeVerRecycleOnList(List<Food> list) {
        this.arrProducts = list;
        this.recyclerView = findViewById(R.id.verRecycle);
        this.homeVerRecycle = new HomeVerRecycle(list, MainActivity.this);
        homeVerRecycle.setAdapterItemAction(new AdapterItemAction() {
            @Override
            public void itemOnCLick(int position) {
                Intent intent = new Intent(MainActivity.this, ShowItemProduct.class);
                addIntentIntoItem(arrProducts, position, intent);
            }

            @Override
            public void onBTNClick(int position) {
                boolean check = listPurchase.stream().anyMatch(food -> food.getName().equals(arrProducts.get(position).getName()));
                if (!check) {
                    listPurchase.add(arrProducts.get(position));
                }
            }
        });
        recyclerView.setAdapter(homeVerRecycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    protected void addIntentIntoItem(List<Food> listData, int position, Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("listData", (Serializable) listData);
        bundle.putSerializable("listDataPurchase", (Serializable) listPurchase);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exits);
    }

    // Phương thức để lấy tên người dùng từ SharedPreferences
    private String getUserNameFromPrefs() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("user_name", "Tên người dùng");
    }

//    private void initInfoUser() {
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        if (firebaseUser != null) {
//            String userId = firebaseUser.getUid();
//            DatabaseReference userRef = database.child("Users").child(userId);
//
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        Users user = snapshot.getValue(Users.class);
//                        if (user != null) {
//                            binding.userNameTv.setText(user.getName());
//                        } else {
//                            Log.e("User", "User is null for snapshot: " + snapshot.toString());
//                        }
//                    } else {
//                        Log.e("User", "Snapshot does not exist");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e("User", "Database error: " + error.getMessage());
//                }
//            });
//        }
//    }


//    private void initBanner() {
//        DatabaseReference bannerRef = database.child("Banners");
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
//        ArrayList<SliderItems> items = new ArrayList<>();
//        bannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        SliderItems sliderItems = dataSnapshot.getValue(SliderItems.class);
//                        if (sliderItems != null) {
//                            items.add(sliderItems);
//                            binding.progressBarBanner.setVisibility(View.GONE);
//                            Log.d("Banner", "Image: " + sliderItems.getImage());
//                        } else {
//                            Log.e("Banner", "SliderItems is null for snapshot: " + dataSnapshot.toString());
//                        }
//                    }
//                    if (items.size() > 0) {
//                        banners(items);
//                    }
//                } else {
//                    Log.e("Banner", "Snapshot does not exist");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Banner", "Database error: " + error.getMessage());
//            }
//        });
//    }

//    private void banners(ArrayList<SliderItems> items) {
//        binding.viewpager2.setAdapter(new SliderAdapter(items, binding.viewpager2));
//        binding.viewpager2.setClipToPadding(false);
//        binding.viewpager2.setClipChildren(false);
//        binding.viewpager2.setOffscreenPageLimit(3);
//        binding.viewpager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
//    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        // thiết lập mục được chọn mặc định
        bottomNavigationView.setItemSelected(R.id.home, true);
        // xử lý sự kiện khi người dùng chọn mục khác nhau
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.cart) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listDataPurchase", (Serializable) listPurchase);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            } else if (id == R.id.profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (id == R.id.orderHistory) {
                startActivity(new Intent(MainActivity.this, OrdersHistoryActivity.class));
            }
        });


        // ẩn layout logout khi click vào nút close
//        binding.logoutBtn.setOnClickListener(v -> {
//            showLogoutConfirmationDialog();
//        });
    }
    // Hiển thị thông báo xác nhận đăng xuất
//    private void showLogoutConfirmationDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Đăng xuất")
//                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
//                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Đăng xuất người dùng
//                        logoutUser();
//                    }
//                })
//                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Đóng thông báo
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }


    // đăng xuất người dùng
//    private void logoutUser() {
//        FirebaseAuth.getInstance().signOut();
//        // xóa thông tin người dùng trong SharedPreferences
//        clearUserInfo();
//
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }

    // xóa thông tin người dùng trong SharedPreferences
//    private void clearUserInfo() {
//        // lấy SharedPreferences và xóa thông tin người dùng
//        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.apply();
//    }

//    private void initCategory() {
//        DatabaseReference categoryRef = database.child("Category");
//        binding.progressBarCategory.setVisibility(View.VISIBLE);
//        ArrayList<Category> categories = new ArrayList<>();
//
//        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        categories.add(dataSnapshot.getValue(Category.class));
//                    }
//                    if (categories.size() > 0) {
//                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
//                        binding.categoryView.setAdapter(new CategoryAdapter(categories));
//                    }
//                    binding.progressBarCategory.setVisibility(View.GONE);
//                } else {
//                    Log.e("Category", "Snapshot does not exist");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Category", "Database error: " + error.getMessage());
//            }
//        });
//    }

//    private void setupSearchFunctionality() {
//        // Xử lý tìm kiếm sản phẩm
//        binding.searchBtn.setOnClickListener(v -> {
//            String searchText = binding.edtSearch.getText().toString().trim();
//            if (!searchText.isEmpty()) {
//                filterProducts(searchText);
//            } else {
//                // Nếu không có văn bản tìm kiếm, hiển thị danh sách ban đầu
//                initCategory(); // Hoặc load lại dữ liệu ban đầu
//            }
//        });
//    }

//    private void filterProducts(String searchText) {
//        DatabaseReference productsRef = database.child("Products");
//        binding.progressBarCategory.setVisibility(View.VISIBLE);
//        ArrayList<Foods> filteredProducts = new ArrayList<>();
//
//        productsRef.orderByChild("title")
//                .startAt(searchText.toLowerCase())
//                .endAt(searchText.toLowerCase() + "\uf8ff")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                Foods product = dataSnapshot.getValue(Foods.class);
//                                if (product != null) {
//                                    filteredProducts.add(product);
//                                }
//                            }
//                            if (filteredProducts.size() > 0) {
//                                binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
//                                // Hiển thị danh sách sản phẩm đã lọc
//                                binding.categoryView.setAdapter(new FoodListAdapter(filteredProducts));
//                            } else {
//                                // Hiển thị thông báo không tìm thấy sản phẩm
//                                Log.d("Search", "No products found with keyword: " + searchText);
//                            }
//                        } else {
//                            // Hiển thị thông báo không tìm thấy sản phẩm
//                            Log.d("Search", "No products found with keyword: " + searchText);
//                        }
//                        binding.progressBarCategory.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("Search", "Database error: " + error.getMessage());
//                        binding.progressBarCategory.setVisibility(View.GONE);
//                    }
//                });
//    }

}
