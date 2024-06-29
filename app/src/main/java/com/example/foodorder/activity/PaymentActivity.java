package com.example.foodorder.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.foodorder.databinding.ActivityPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        mAuth = FirebaseAuth.getInstance();

        binding.submitBtn.setOnClickListener(v -> placeOrder());
    }
    private void placeOrder() {
        // lấy thông tin địa chỉ và phương thức thanh toán từ người dùng
        String address = binding.addressEdt.getText().toString().trim();
        String paymentMethod;
        if (binding.paymentRdBtn.isChecked()) {
            paymentMethod = "Thanh toán khi nhận hàng";
        } else {
            paymentMethod = "Other payment method"; // Customize as needed
        }
        // Generate a random orderId
        String orderId = UUID.randomUUID().toString().toUpperCase();

        // lấy id của người dùng hiện tại
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = "";
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        // lấy thông tin các món ăn trong giỏ hàng
        // và tính tổng tiền
        double itemTotal = 100.0;
        double tax = 10.0;
        double deliveryFee = 5.0;
        double total = itemTotal + tax + deliveryFee;


        // tạo một đối tượng Orders
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("address", address);
        order.put("paymentMethod", paymentMethod);
        order.put("itemTotal", itemTotal);
        order.put("tax", tax);
        order.put("deliveryFee", deliveryFee);
        order.put("total", total);


        // lưu thông tin đơn hàng vào database
        databaseReference.push().setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PaymentActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after placing order
                })
                .addOnFailureListener(e -> Toast.makeText(PaymentActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show());
    }
}