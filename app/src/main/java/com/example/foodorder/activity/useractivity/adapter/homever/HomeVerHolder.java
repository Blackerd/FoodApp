package com.example.foodorder.activity.useractivity.adapter.homever;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.AdapterItemAction;

public class HomeVerHolder extends RecyclerView.ViewHolder {
    public ImageView imageView ;
    public TextView textProductName ,textProductPrice,textProductType ;
    public Button button ;
    public HomeVerHolder(@NonNull View itemView, AdapterItemAction homeVerRecycle) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.pic);
        this.textProductName = itemView.findViewById(R.id.titleTxt);
        this.textProductType = itemView.findViewById(R.id.type_pro);
        this.textProductPrice = itemView.findViewById(R.id.feeTxt);
        this.button = itemView.findViewById(R.id.addtocart);
        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            homeVerRecycle.itemOnCLick(position);});
    }
}
