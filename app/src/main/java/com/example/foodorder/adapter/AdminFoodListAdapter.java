package com.example.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.domain.Foods;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminFoodListAdapter extends RecyclerView.Adapter<AdminFoodListAdapter.FoodViewHolder> {
    private final List<Foods> foodsList;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Foods food);
        void onDeleteClick(Foods food);
    }

    public AdminFoodListAdapter(Context context, List<Foods> foodsList, OnItemClickListener listener) {
        this.context = context;
        this.foodsList = foodsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_viewholder, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Foods food = foodsList.get(position);
        holder.titleTxt.setText(food.getTitle());
        holder.priceTxt.setText(String.format("$%.2f", food.getPrice()));
        holder.ratingTxt.setText(String.valueOf(food.getStar()));
        holder.timeTxt.setText(String.valueOf(food.getTimeValue()));

        Picasso.get().load(food.getImagePath()).into(holder.imageFood);

        holder.editImage.setOnClickListener(v -> listener.onEditClick(food));
        holder.deleteImage.setOnClickListener(v -> listener.onDeleteClick(food));
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, ratingTxt, timeTxt;
        ImageView imageFood, editImage, deleteImage;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            imageFood = itemView.findViewById(R.id.imageFood);
            editImage = itemView.findViewById(R.id.editImage);
            deleteImage = itemView.findViewById(R.id.deleteImage);
        }
    }
}
