package com.example.foodorder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityOrderBinding;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.domain.Orders;
import com.example.foodorder.helper.ManagmentCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    ActivityOrderBinding binding;
    DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo DatabaseReference để thao tác với dữ liệu trên Firebase Realtime Database
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Nhận dữ liệu từ CartActivity
        ArrayList<Foods> cartItems = (ArrayList<Foods>) getIntent().getSerializableExtra("cartItems");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        double itemTotal = getIntent().getDoubleExtra("itemTotal", 0.0);
        double deliveryFee = getIntent().getDoubleExtra("deliveryFee", 0.0);
        double tax = getIntent().getDoubleExtra("tax", 0.0);

        // Hiển thị dữ liệu giỏ hàng
        binding.totalTxt.setText("$" + String.valueOf(totalAmount));
        binding.totalFreeTxt.setText("$" + String.valueOf(itemTotal));
        binding.deliveryTxt.setText("$" + String.valueOf(deliveryFee));
        binding.taxTxt.setText("$" + String.valueOf(tax));

        binding.submitBtn.setOnClickListener(v -> placeOrder(cartItems, itemTotal, tax, deliveryFee, totalAmount));

        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void placeOrder(ArrayList<Foods> cartItems, double itemTotal, double tax, double deliveryFee, double totalAmount) {
        // Lấy địa chỉ giao hàng từ EditText
        String address = binding.addressEdt.getText().toString();

        // Lấy id của user hiện tại từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("user_id", -1);
        String userName = sharedPref.getString("user_name", "");

        if (userId == -1) {
            Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy orderId mới
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long maxOrderId = 0;
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    long orderId = Long.parseLong(orderSnapshot.getKey());
                    if (orderId > maxOrderId) {
                        maxOrderId = orderId;
                    }
                }
                long newOrderId = maxOrderId + 1;

                // Tạo danh sách các món ăn trong đơn hàng
                List<String> orderedItems = new ArrayList<>();
                for (Foods item : cartItems) {
                    // Thêm tên và số lượng của mỗi món ăn vào danh sách đơn hàng
                    String itemName = item.getTitle();
                    int quantity = item.getNumberInCart();
                    orderedItems.add(itemName + " x" + quantity);
                }

                // Tạo đối tượng đơn hàng với status mặc định là 0 (chờ xác nhận)
                Orders order = new Orders(userName, (int) newOrderId, userId, orderedItems, itemTotal, tax, deliveryFee, totalAmount, address, new Date());
                order.setStatus(Orders.STATUS_PENDING); // Đặt trạng thái mặc định là "Chờ xác nhận"

                // Lưu đơn hàng vào Firebase Realtime Database
                ordersRef.child(String.valueOf(newOrderId)).setValue(order, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Xóa giỏ hàng sau khi đặt hàng thành công
                            new ManagmentCart(OrderActivity.this).clearCart();

                            // Hiển thị thông báo đặt hàng thành công
                            Toast.makeText(OrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                            // Chuyển người dùng về màn hình main
                            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Xử lý khi có lỗi xảy ra trong quá trình đặt hàng
                            Toast.makeText(OrderActivity.this, "Đã xảy ra lỗi khi đặt hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra khi đọc dữ liệu
                Toast.makeText(OrderActivity.this, "Đã xảy ra lỗi khi tạo đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
