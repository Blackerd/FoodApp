package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityVerifyOtpactivityBinding;

public class VerifyOTPActivity extends AppCompatActivity {
    ActivityVerifyOtpactivityBinding binding;
    String email;
    String otpSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("email") && getIntent().hasExtra("otp")) {
            email = getIntent().getStringExtra("email");
            otpSent = getIntent().getStringExtra("otp");
        } else {
            Toast.makeText(this, "Không có thông tin email hoặc OTP", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.verifyBtn.setOnClickListener(v -> {
            String userEnteredOTP = binding.otpEdt.getText().toString();

            if (userEnteredOTP.equals(otpSent)) {
                // Nếu mã OTP nhập đúng, chuyển đến trang tạo mật khẩu mới
            } else {
                Toast.makeText(VerifyOTPActivity.this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
