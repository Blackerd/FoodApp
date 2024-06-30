package com.example.foodorder.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load user information from SharedPreferences
        loadUserInfo();
        setVariables();
    }

    private void loadUserInfo() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        int userId = sharedPref.getInt("user_id", -1);
        String userEmail = sharedPref.getString("user_email", "");
        String userName = sharedPref.getString("user_name", "");

        if (userId != -1 && !userEmail.isEmpty()) {
            // Update UI with user information
            binding.textView21.setText(userName);
            binding.textView22.setText(userEmail);

            // Load profile image using Glide with the correct binding
            Glide.with(this)
                    .load("https://i.pravatar.cc/150?img=" + userId)
                    .placeholder(R.drawable.profile) // Placeholder image while loading
                    .error(R.drawable.profile) // Image to show if loading fails
                    .into(binding.imageView11); // Use binding to access imageView11 directly

        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVariables() {
        bottomNavigationView = findViewById(R.id.bottomMenuUser);
        // Set the default selected item
        bottomNavigationView.setItemSelected(R.id.profile, true);
        // Handle item selection events
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            } else if (id == R.id.cart) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
            } // No need to handle profile item again

        });

        // Handle logout button click
        binding.logoutBtn.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });
    }

    // Show logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Logout user
                        logoutUser();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Logout user
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // Clear user information from SharedPreferences
        clearUserInfo();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Clear user information from SharedPreferences
    private void clearUserInfo() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
