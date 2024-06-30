package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.sendBtn.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString().trim();

            if (email.isEmpty()) {
                binding.emailEdt.setError("Email không được để trống");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEdt.setError("Email không hợp lệ");
                return;
            }

            // Gửi mã OTP từ Firebase Authentication
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lấy mã OTP và chuyển sang VerifyOTPActivity
                            String otp = generateRandomOTP(); // Tạo mã OTP ngẫu nhiên
                            Intent intent = new Intent(ForgetPasswordActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("otp", otp); // Truyền mã OTP sang VerifyOTPActivity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Gửi thất bại. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to generate a 4-digit OTP
    private String generateRandomOTP() {
        // Generate a random number between 1000 and 9999
        int otp = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(otp);
    }
}
