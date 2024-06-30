package com.example.foodorder.activity.useractivity.adapter.bag;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BagItemHolderRecycle extends RecyclerView.Adapter<BagItemRecycle> {
    private List<Food> list;

    public OnBTNClick onbtnClick;
    public void setOnBTNClick(OnBTNClick onBTNClick) {
        this.onbtnClick = onBTNClick;
    }
    public BagItemHolderRecycle(List<Food> list) {
        this.list = list;

    }

    @NonNull
    @Override
    public BagItemRecycle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy, parent, false);
        return new BagItemRecycle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BagItemRecycle holder, int position) {
        int quantity =0;
        int index = position;
        Food food = this.list.get(position);
        Picasso.get().load(food.getUrl()).into(holder.imageViewBag);
        holder.textProductName.setText(food.getName());
        holder.textProductPrice.setText(food.getPrice()+"00 VND");
        holder.textProductType.setText("Type :"+food.getType());
        holder.textQuantity.setText(""+quantity);
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbtnClick.increment(index,holder.textQuantity );
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbtnClick.decrement(index,holder.textQuantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }

    public interface OnBTNClick {
        public void increment(int potiton, TextView view );

        public void decrement(int position, TextView view );
    }
}
