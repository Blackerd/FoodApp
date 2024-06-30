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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

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
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                binding.emailEdt.setError("Email is required");
                binding.passwordEdt.setError("Password is required");
                return;
            }

            databaseReference.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    Users users = userSnapshot.getValue(Users.class);
                                    if (users != null && BCrypt.checkpw(password, users.getPassword())) {
                                        Log.d(TAG, "User logged in: " + users.getId());

                                        saveUserInfo(users);

                                        if (users.isAdmin()) {
                                            Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        binding.passwordEdt.setError("Email hoặc password không đúng");
                                    }
                                }
                            } else {
                                binding.emailEdt.setError("Email hoặc password không đúng");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "Failed to read user role", databaseError.toException());
                            Toast.makeText(LoginActivity.this, "Failed to read user role", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        binding.forgetPassTxt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }

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
