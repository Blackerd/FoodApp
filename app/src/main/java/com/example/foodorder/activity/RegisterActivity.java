package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityRegisterBinding;
import com.example.foodorder.domain.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRegister();
    }

    private void initRegister() {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        binding.registerBtn.setOnClickListener(v -> {
            String userName = binding.nameEdt.getText().toString();
            String email = binding.emailEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();
            String confirmPassword = binding.cfpasswordEdt.getText().toString();

            if  ( userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                if (userName.isEmpty()) {
                    binding.nameEdt.setError("Họ tên không được để trống");
                }
                if (email.isEmpty()) {
                    binding.emailEdt.setError("Email không được để trống");
                }
                if (password.isEmpty()) {
                    binding.passwordEdt.setError("Mật khẩu không được để trống");
                }
                if (confirmPassword.isEmpty()) {
                    binding.cfpasswordEdt.setError("Xác nhận mật khẩu không được để trống");
                }
                return;
            }

            if (!password.equals(confirmPassword)) {
                binding.cfpasswordEdt.setError("Mật khẩu xác nhận không khớp");
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int newId = 0;
                                        if (dataSnapshot.exists()) {
                                            // Lấy id lớn nhất hiện có và cộng thêm 1
                                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                Users existingUser = userSnapshot.getValue(Users.class);
                                                if (existingUser != null && existingUser.getId() > newId) {
                                                    newId = existingUser.getId();
                                                }
                                            }
                                            newId += 1;
                                        }

                                        Users newUser = new Users();
                                        newUser.setName(userName);
                                        newUser.setEmail(email);
                                        newUser.setPassword(password);
                                        newUser.setAdmin(false);
                                        newUser.setId(newId);

                                        databaseReference.child(String.valueOf(newId)).setValue(newUser)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Log.d(TAG, "User registered successfully");
                                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Log.e(TAG, "User registration failed", task1.getException());
                                                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "Failed to get users", databaseError.toException());
                                    }
                                });
                            }
                        } else {
                            Log.e(TAG, "Registration failed: " + task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
