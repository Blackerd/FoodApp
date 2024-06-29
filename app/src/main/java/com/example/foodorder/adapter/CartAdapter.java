package com.example.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodorder.R;
import com.example.foodorder.domain.Foods;
import com.example.foodorder.helper.ChangeNumberItemsListener;
import com.example.foodorder.helper.ManagmentCart;

import java.util.ArrayList;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.viewholder>{
    ArrayList<Foods> foods;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> foods, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener) {
        this.foods = foods;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.titleTxt.setText(foods.get(position).getTitle());
        holder.feeEachTime.setText("$" + foods.get(position).getPrice());
        holder.numberItem.setText(foods.get(position).getNumberInCart() + "");

        Glide.with(holder.itemView)
                .load(foods.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> managmentCart.plusNumberItem(foods, position, () -> {
            notifyDataSetChanged();
              changeNumberItemsListener.change();

        }));

        holder.minusItem.setOnClickListener(v -> managmentCart.minusNumberItem(foods, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.trashBtn.setOnClickListener(v -> managmentCart.removeItem(foods, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeEachTime, plusItem, minusItem, numberItem;
        ImageView pic;
        ConstraintLayout trashBtn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeEachTime = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            numberItem = itemView.findViewById(R.id.numberItemTxt);
            pic = itemView.findViewById(R.id.pic);
            trashBtn = itemView.findViewById(R.id.trashBtn);
        }
    }
}
