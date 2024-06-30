package com.example.foodorder.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityDetailBinding;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.helper.ManagmentCart;

public class DetailActivity extends HomeActivity {
    ActivityDetailBinding binding;
    private Foods food;
    private int numberInCart = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(this)
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.imageFood);

        binding.priceTxt.setText("$"+food.getPrice());
        binding.titleTxt.setText(food.getTitle());
        binding.desccriptionTxt.setText(food.getDescription());
        binding.ratingTxt.setText(food.getStar() + "Rating");
        binding.ratingBar.setRating((float) food.getStar());
        binding.totalTxt.setText(numberInCart*food.getPrice() + "$");

        binding.plusTxt.setOnClickListener(v -> {
            numberInCart = numberInCart + 1;
            binding.numTxt.setText(numberInCart + "");
            binding.totalTxt.setText(numberInCart*food.getPrice() + "$");
        });

        binding.minusTxt.setOnClickListener(v -> {
            if (numberInCart > 1) {
                numberInCart = numberInCart - 1;
                binding.numTxt.setText(numberInCart + "");
                binding.totalTxt.setText(numberInCart*food.getPrice() + "$");
            }
        });

        binding.addToCartBtn.setOnClickListener(v -> {
            food.setNumberInCart(numberInCart);
            managmentCart.insertFood(food);

        });

    }

    private void getIntentExtra() {
        food = (Foods) getIntent().getSerializableExtra("Food");
    }
}