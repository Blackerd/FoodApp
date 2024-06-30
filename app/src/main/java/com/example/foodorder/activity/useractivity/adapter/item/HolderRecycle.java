package com.example.foodorder.activity.useractivity.adapter.item;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.home.HomeHolderRecycleAdapter;

public class HolderRecycle extends RecyclerView.ViewHolder {
    public ImageView show_item_img;
    public HolderRecycle(@NonNull View itemView, HomeHolderRecycleAdapter.OnDataRecycleAdapter onDataRecycleAdapter) {
        super(itemView);
//        nơi ánh xa và gán giá tri
        this.show_item_img = itemView.findViewById(R.id.imageView);
        this.show_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                onDataRecycleAdapter.onItemClick(position);
            }
        });
    }
}
