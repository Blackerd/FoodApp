package com.example.foodorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodorder.R;
import com.example.foodorder.activity.DetailActivity;
import com.example.foodorder.domain.Foods;

import java.util.ArrayList;
import java.util.Locale;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private ArrayList<Foods> foods;
    private ArrayList<Foods> foodsFiltered; // Danh sách sản phẩm đã lọc
    private Context context;

    public FoodListAdapter(ArrayList<Foods> foods) {
        this.foods = foods;
        this.foodsFiltered = new ArrayList<>(foods); // Sao chép danh sách ban đầu
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lấy ra context từ parent
        context = parent.getContext();
        // Gán layout cho viewholder
        // Trả về viewholder
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.food_list_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = foodsFiltered.get(position);
        holder.titleTxt.setText(food.getTitle());
        holder.priceTxt.setText("$" + food.getPrice());
        holder.ratingTxt.setText(food.getStar() + " Rating");
        holder.timeTxt.setText(food.getTimeValue() + " mins");

        // Kiểm tra ImagePath trước khi load ảnh
        String imagePath = food.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            // Load ảnh từ url
            Glide.with(context)
                    .load(imagePath)
                    .transform(new CenterCrop(), new RoundedCorners(60))
                    .into(holder.foodImage);
        } else {
            // Xử lý trường hợp ImagePath là null
            // Ví dụ: Load ảnh placeholder
            Glide.with(context)
                    .load(R.drawable.profile) // Thay thế bằng hình ảnh placeholder hoặc một hình ảnh mặc định khác
                    .transform(new CenterCrop(), new RoundedCorners(60))
                    .into(holder.foodImage);
        }

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            // Chuyển sang màn hình chi tiết món ăn
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Food", food);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodsFiltered.size();
    }

    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());
        foodsFiltered.clear();
        if (searchText.length() == 0) {
            foodsFiltered.addAll(foods);
        } else {
            for (Foods food : foods) {
                if (food.getTitle().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    foodsFiltered.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, ratingTxt, timeTxt;
        ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ itemView
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            foodImage = itemView.findViewById(R.id.imageFood);
        }
    }
}
