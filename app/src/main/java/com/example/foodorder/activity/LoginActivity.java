package com.example.foodorder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityLoginBinding;
import com.example.foodorder.domain.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLogin();
    }

    private void initLogin() {
        // lấy thông tin user từ database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // xử lý sự kiện khi click vào nút login
        binding.loginBtn.setOnClickListener(v -> {
            // lấy thông tin email và password từ form
            String email = binding.emailEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();

            // kiểm tra email và password có rỗng không
            if (email.isEmpty() || password.isEmpty()) {
                binding.emailEdt.setError("Email is required");
                binding.passwordEdt.setError("Password is required");
                return;
            }

            // đăng nhập vào hệ thống bằng email và password
            auth.signInWithEmailAndPassword(email, password)
                    // xử lý kết quả trả về
                    .addOnCompleteListener(task -> {
                        // nếu đăng nhập thành công thì kiểm tra vai trò người dùng
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                Log.d(TAG, "User logged in: " + user.getUid());

                                // lấy thông tin vai trò người dùng từ database
                                databaseReference.orderByChild("email").equalTo(email)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // kiểm tra xem có dữ liệu không
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                        Users users = userSnapshot.getValue(Users.class);
                                                        if (users != null) {
                                                            Log.d(TAG, "User role: " + (users.isAdmin() ? "Admin" : "User"));

                                                            // Lưu thông tin người dùng vào SharedPreferences
                                                            saveUserInfo(users);

                                                            // nếu là admin thì chuyển sang màn hình admin
                                                            if (users.isAdmin()) {
                                                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // nếu là user thì chuyển sang màn hình user
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } else {
                                                            Log.e(TAG, "User data is null");
                                                        }
                                                    }
                                                } else {
                                                    Log.e(TAG, "User data does not exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.e(TAG, "Failed to read user role", databaseError.toException());
                                                Toast.makeText(LoginActivity.this, "Failed to read user role", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "Login failed: " + task.getException().getMessage());
                            binding.emailEdt.setError("Email hoặc password không đúng");
                        }
                    });
        });

        binding.registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Lưu thông tin người dùng vào SharedPreferences
    private void saveUserInfo(Users user) {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_email", user.getEmail());
        editor.putBoolean("is_admin", user.isAdmin());
        editor.putString("user_name", user.getName());
        editor.apply();
    }
}
