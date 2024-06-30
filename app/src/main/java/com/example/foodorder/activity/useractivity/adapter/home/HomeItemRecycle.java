package com.example.foodorder.activity.useractivity.adapter.home;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;

public class HomeItemRecycle extends RecyclerView.ViewHolder {
  public   ImageView imageView ;
  public  TextView textProductName ,textProductPrice,textProductType ;
  public Button button ;

    public HomeItemRecycle(@NonNull View itemView , HomeHolderRecycleAdapter.OnDataRecycleAdapter onDataRecycleAdapter){
        super(itemView);
        this.imageView = itemView.findViewById(R.id.imageProduct);
        this.textProductName = itemView.findViewById(R.id.textProductName);
        this.textProductPrice = itemView.findViewById(R.id.textProductPrice);
        this.textProductType = itemView.findViewById(R.id.textProductType);
        this.button = itemView.findViewById(R.id.buttonProduct);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                onDataRecycleAdapter.onItemClick(position);
            }
        });
    }


}
