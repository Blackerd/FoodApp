package com.example.foodorder.activity.useractivity.adapter.bag;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;

public class BagItemRecycle extends RecyclerView.ViewHolder {
    public   ImageView imageViewBag ;
    public TextView textProductName ,textProductPrice,textProductType,textQuantity ;
    public Button increment,decrement ;

    public BagItemRecycle(@NonNull View itemView) {
        super(itemView);
        this.imageViewBag = itemView.findViewById(R.id.imageProductBag);
        this.textProductName = itemView.findViewById(R.id.textProductNameBag);
        this.textProductPrice = itemView.findViewById(R.id.textProductPriceBag);
        this.textProductType = itemView.findViewById(R.id.textProductTypeBag);
        this.textQuantity = itemView.findViewById(R.id.numberBag);
        this.increment = itemView.findViewById(R.id.increment);
        this.decrement = itemView.findViewById(R.id.decrement);
    }
}
