package com.example.foodorder.activity.useractivity.adapter.item;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.home.HomeHolderRecycleAdapter;
import com.example.foodorder.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowItemRecycleAdapter extends RecyclerView.Adapter<HolderRecycle> {
    private List<Food> list;
    HomeHolderRecycleAdapter.OnDataRecycleAdapter onDataRecycleAdapter;

    public void setOnClick(HomeHolderRecycleAdapter.OnDataRecycleAdapter onDataRecycleAdapter) {
        this.onDataRecycleAdapter = onDataRecycleAdapter;

    }


    public ShowItemRecycleAdapter(List<Food> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public HolderRecycle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        custom giao diện load ra theo từng item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_item, parent, false);
        return new HolderRecycle(view, this.onDataRecycleAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecycle holder, int position) {
//        nó sẽ lấy content từ holder đê set content from list
        Food item = this.list.get(position);
        Picasso.get().load(item.getUrl()).into(holder.show_item_img);


    }

    @Override
//    nơi chưa độ dài mà nó sẽ trả ra để lặp
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }

}
