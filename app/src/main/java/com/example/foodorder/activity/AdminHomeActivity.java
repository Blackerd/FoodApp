package com.example.foodorder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityAdminHomeBinding;
import com.example.foodorder.domain.Orders;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminHomeActivity extends AppCompatActivity {
    ActivityAdminHomeBinding binding;
    private DatabaseReference ordersRef;
    private ChipNavigationBar bottomNavigationView;
    private PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo biến ordersRef để tham chiếu đến node Orders trên Firebase
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        // Ánh xạ view của biểu đồ hình tròn
        pieChart = findViewById(R.id.pieChart);

        // Hiển thị tên người dùng lên TextView
        TextView userNameTextView = findViewById(R.id.userName);
        userNameTextView.setText(getUserNameFromPrefs());

        // Lấy dữ liệu đơn hàng từ Firebase
        fetchOrders();
        // Thiết lập biến và xử lý sự kiện
        setVariables();
    }

    // Phương thức để lấy tên người dùng từ SharedPreferences
    private String getUserNameFromPrefs() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("user_name", "Tên người dùng");
    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuAmin);
        bottomNavigationView.setItemSelected(R.id.home, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
            } else if (id == R.id.manage_orders) {
                startActivity(new Intent(AdminHomeActivity.this, OrderManagerActivity.class));
            } else if (id == R.id.manage_products) {
                startActivity(new Intent(AdminHomeActivity.this, ProductsManagerActivity.class));
            } else if (id == R.id.manage_users) {
                startActivity(new Intent(AdminHomeActivity.this, UsersManagerActivity.class));
            }
        });

        // ẩn layout logout khi click vào nút close
        binding.logoutBtn.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });
    }
    // Hiển thị thông báo xác nhận đăng xuất
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Đăng xuất người dùng
                        logoutUser();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng thông báo
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    // đăng xuất người dùng
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // xóa thông tin người dùng trong SharedPreferences
        clearUserInfo();

        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
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


    private void fetchOrders() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Float> dateRevenueMap = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders order = snapshot.getValue(Orders.class);
                    if (order != null) {
                        // Lấy thời gian của đơn hàng và tổng số tiền
                        long orderTime = order.getOrderDate().getTime(); // Thời gian đơn hàng
                        float totalAmount = (float) order.getTotal(); // Tổng số tiền

                        // Lấy ngày từ thời gian của đơn hàng
                        String orderDate = sdf.format(new Date(orderTime));

                        // Tính tổng doanh thu theo ngày
                        if (dateRevenueMap.containsKey(orderDate)) {
                            dateRevenueMap.put(orderDate, dateRevenueMap.get(orderDate) + totalAmount);
                        } else {
                            dateRevenueMap.put(orderDate, totalAmount);
                        }
                    }
                }

                // Thiết lập biểu đồ hình tròn
                setupPieChart(dateRevenueMap);

                // Tính toán và hiển thị tổng tiền và số đơn hàng trong ngày hôm nay
                calculateDailyRevenue(dateRevenueMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    private void setupPieChart(Map<String, Float> dateRevenueMap) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : dateRevenueMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        // Tạo dataset cho biểu đồ từ các PieEntry
        PieDataSet dataSet = new PieDataSet(entries, "Doanh thu");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Sử dụng bảng màu có sẵn
        dataSet.setSliceSpace(3f); // Khoảng cách giữa các phần của biểu đồ
        dataSet.setSelectionShift(5f); // Hiệu ứng khi chọn vào phần của biểu đồ

        // Định dạng số liệu doanh thu
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "$%.2f", value); // Định dạng giá trị với dấu $
            }
        });

        // Tạo dữ liệu biểu đồ từ dataset
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false); // Tắt mô tả biểu đồ
        pieChart.setDrawHoleEnabled(true); // Hiển thị lỗ ở giữa biểu đồ
        pieChart.setHoleColor(Color.WHITE); // Màu của lỗ ở giữa biểu đồ
        pieChart.setHoleRadius(30f); // Đặt bán kính của lỗ ở giữa biểu đồ
        pieChart.setTransparentCircleRadius(50f); // Đường tròn trong suốt xung quanh lỗ
        pieChart.setEntryLabelTextSize(12f); // Kích thước chữ của các phần
        pieChart.setEntryLabelColor(Color.BLACK); // Màu chữ của các phần

        // Cập nhật biểu đồ
        pieChart.invalidate();
    }
    // Tính toán và hiển thị tổng tiền và số đơn hàng trong ngày hôm nay
    private void calculateDailyRevenue(Map<String, Float> dateRevenueMap) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("Orders");

        // Lấy ngày hôm nay
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Lấy ngày hôm nay dưới dạng chuỗi
        String today = sdf.format(new Date());

        // Tổng doanh thu trong ngày hôm nay
        float totalRevenueToday = dateRevenueMap.containsKey(today) ? dateRevenueMap.get(today) : 0f;
        // Số đơn hàng trong ngày hôm nay
        final int[] totalOrdersToday = {0};

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Orders order = orderSnapshot.getValue(Orders.class);
                    if (order != null) {
                        String orderDate = sdf.format(order.getOrderDate());

                        if (orderDate.equals(today)) {
                            totalOrdersToday[0]++;
                        }
                    }
                }

                // Cập nhật giao diện người dùng
                binding.totalTxt.setText(String.format(Locale.getDefault(), "%.2f$", totalRevenueToday));
                binding.totalOrderTxt.setText(String.format(Locale.getDefault(), "%d đơn hàng", totalOrdersToday[0]));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}


