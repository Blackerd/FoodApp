package com.example.foodorder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.domain.Foods;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEditProductActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, priceEditText, starEditText, timeEditText;
    private Button saveButton, cancelButton;
    private Foods food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_product);

        titleEditText = findViewById(R.id.productTitleEditText);
        descriptionEditText = findViewById(R.id.productDescriptionEditText);
        priceEditText = findViewById(R.id.productPriceEditText);
        starEditText = findViewById(R.id.productStarEditText);
        timeEditText = findViewById(R.id.productTimeEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        food = (Foods) getIntent().getSerializableExtra("Food");
        if (food != null) {
            // Chỉnh sửa sản phẩm hiện tại
            titleEditText.setText(food.getTitle());
            descriptionEditText.setText(food.getDescription());
            priceEditText.setText(String.valueOf(food.getPrice()));
            starEditText.setText(String.valueOf(food.getStar()));
            timeEditText.setText(String.valueOf(food.getTimeValue()));
        }

        saveButton.setOnClickListener(v -> saveProduct());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveProduct() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        float star = Float.parseFloat(starEditText.getText().toString().trim());
        int time = Integer.parseInt(timeEditText.getText().toString().trim());

        if (food == null) {
            // Thêm sản phẩm mới
            String id = FirebaseDatabase.getInstance().getReference("foods").push().getKey();
            Foods newFood = new Foods();
            newFood.setId(Integer.parseInt(id));
            newFood.setTitle(title);
            newFood.setDescription(description);
            newFood.setPrice(price);
            newFood.setStar(star);
            newFood.setTimeValue(time);
            FirebaseDatabase.getInstance().getReference("foods").child(id).setValue(newFood)
                    .addOnSuccessListener(aVoid -> finish())
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                    });
        } else {
            // Cập nhật sản phẩm hiện tại
            food.setTitle(title);
            food.setDescription(description);
            food.setPrice(price);
            food.setStar(star);
            food.setTimeValue(time);
            FirebaseDatabase.getInstance().getReference("foods").child(String.valueOf(food.getId())).setValue(food)
                    .addOnSuccessListener(aVoid -> finish())
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                    });
        }
    }
}
