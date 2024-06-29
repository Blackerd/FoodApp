package com.example.foodorder.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.adapter.OrderAdapter;
import com.example.foodorder.domain.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerActivity extends AppCompatActivity implements OrderAdapter.OrderClickListener {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Orders> orderList;
    private ChipNavigationBar bottomNavigationView;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);

        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        fetchOrders();
    }
    // Hàm này sẽ khởi tạo các view
    private void initViews() {
        ordersRecyclerView = findViewById(R.id.ordersView);
        bottomNavigationView = findViewById(R.id.bottomMenuAmin);
    }
    // Hàm này sẽ khởi tạo RecyclerView và Adapter
    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList, this);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(orderAdapter);
    }
    // Hàm này sẽ khởi tạo Bottom Navigation
    private void setupBottomNavigation() {
        bottomNavigationView.setItemSelected(R.id.manage_orders, true);
        bottomNavigationView.setOnItemSelectedListener(this::onBottomNavigationItemSelected);
    }

    // Hàm này sẽ được gọi khi người dùng chọn một item trên bottom navigation
    private void onBottomNavigationItemSelected(int id) {
        Intent intent = null;
        if (id == R.id.home) {
            intent = new Intent(this, AdminHomeActivity.class);
        } else if (id == R.id.manage_orders) {
            return;
        } else if (id == R.id.manage_products) {
            intent = new Intent(this, ProductsManagerActivity.class);
        } else if (id == R.id.manage_users) {
            intent = new Intent(this, UsersManagerActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    // Hàm này sẽ lấy dữ liệu từ Firebase Realtime Database
    private void fetchOrders() {
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orders order = snapshot.getValue(Orders.class);
                    orderList.add(order);
                }
                // Cập nhật dữ liệu mới nhất lên RecyclerView
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrderManagerActivity", "Failed to read orders", databaseError.toException());
            }
        });
    }

    // Hàm này sẽ được gọi khi người dùng chọn một trạng thái mới cho đơn hàng
    @Override
    public void onUpdateStatusClick(Orders order) {
        List<String> statusOptions = getStatusOptions(order.getStatus());
        showStatusOptionsDialog(order, statusOptions);
    }

    // Hàm này sẽ cập nhật trạng thái mới cho đơn hàng
    private void updateOrderStatus(Orders order, int status) {
        ordersRef.child(String.valueOf(order.getOrderId())).child("status").setValue(status)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        order.setStatus(status);
                        order.setStatusString(Orders.getStatusString(status));
                        orderAdapter.notifyDataSetChanged();
                    }
                });
        ordersRef.child(String.valueOf(order.getOrderId())).child("statusString")
                .setValue(Orders.getStatusString(status));
    }

    private void showConfirmationDialog(Orders order, final int status) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận cập nhật trạng thái")
                .setMessage("Bạn có chắc chắn muốn cập nhật trạng thái mới không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> updateOrderStatus(order, status))
                .setNegativeButton("Hủy bỏ", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showStatusOptionsDialog(final Orders order, List<String> statusOptions) {
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái")
                .setItems(statusOptions.toArray(new String[0]), (dialog, which) -> {
                    int newStatus = getStatusValue(statusOptions.get(which));
                    if (newStatus != -1) {
                        showConfirmationDialog(order, newStatus);
                    }
                })
                .show();
    }

    private List<String> getStatusOptions(int currentStatus) {
        List<String> options = new ArrayList<>();
        switch (currentStatus) {
            case Orders.STATUS_PENDING:
                options.add("Đã xác nhận");
                options.add("Đã hủy");
                options.add("Đang giao");
                break;
            case Orders.STATUS_CONFIRMED:
                options.add("Đang giao");
                break;
            case Orders.STATUS_DELIVERING:
                options.add("Thành công");
                options.add("Đã hủy");
                break;
        }
        return options;
    }

    private int getStatusValue(String statusString) {
        switch (statusString) {
            case "Chờ xác nhận":
                return Orders.STATUS_PENDING;
            case "Đã xác nhận":
                return Orders.STATUS_CONFIRMED;
            case "Đã hủy":
                return Orders.STATUS_CANCELLED;
            case "Đang giao":
                return Orders.STATUS_DELIVERING;
            case "Thành công":
                return Orders.STATUS_COMPLETED;
            default:
                return -1;
        }
    }

    @Override
    public void onAcceptClick(Orders order) {
        updateOrderStatus(order, Orders.STATUS_CONFIRMED);
    }

    @Override
    public void onCancelClick(Orders order) {
        updateOrderStatus(order, Orders.STATUS_CANCELLED);
    }
}
