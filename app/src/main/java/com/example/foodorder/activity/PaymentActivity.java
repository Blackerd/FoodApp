package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityPaymentBinding;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.retrofit.user.UserServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaymentActivity extends AppCompatActivity {
    List<Food> listChooseds;
    int total, itemsPrice;

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

        Bundle bundle = getIntent().getBundleExtra("data");
        this.listChooseds = (List<Food>) bundle.getSerializable("itemChooseds");
        this.total = bundle.getInt("total");
        this.itemsPrice = bundle.getInt("itemprice");
        binding.totalTxt.setText("" + this.total + ".000 VND");
        binding.totalFreeTxt.setText(itemsPrice + ".000 VND");
        binding.deliveryTxt.setText("30.000 VND");
        binding.taxTxt.setText("0 VND");
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

        List<String> list = (List<String>) this.listChooseds.stream().map(food -> "" + food.getName() + " x" + food.quantity).collect(Collectors.toList());

        // tạo một đối tượng Orders
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("address", address);
        order.put("paymentMethod", paymentMethod);
        order.put("itemTotal", this.itemsPrice);
        order.put("tax", 0);
        order.put("items", list);
        order.put("deliveryFee", 30);
        order.put("total", this.total);
        order.put("date",new Date());
//        UserServices userServices = new UserServices();
//        userServices.order(userId, this.total, order, new GetOnDataListener() {
//            @Override
//            public void onSuccess(Object o) {
//                Toast.makeText(PaymentActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
//            }
//            @Override
//            public void onFailure(Object o) {
//            }
//        });

//         lưu thông tin đơn hàng vào database
            databaseReference.push().setValue(order)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> Toast.makeText(PaymentActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show());

    }
}