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
import com.example.foodorder.R;
import com.example.foodorder.activity.ListFoodActivity;
import com.example.foodorder.domain.Category;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
       holder.tvName.setText(items.get(position).getName());

       Glide.with(context)
               .load(items.get(position).getImagePath())
               .into(holder.imgCategory);
        // xử lý sự kiện click vào item
       holder.itemView.setOnClickListener(v -> {
           // chuyển sang màn hình danh sách món ăn
           Intent intent = new Intent(context, ListFoodActivity.class);
           intent.putExtra("CategoryId", items.get(position).getId());
           intent.putExtra("CategoryName", items.get(position).getName());
           context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgCategory;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tVCategory);
            imgCategory = itemView.findViewById(R.id.imgCategory);

        }
    }
}
