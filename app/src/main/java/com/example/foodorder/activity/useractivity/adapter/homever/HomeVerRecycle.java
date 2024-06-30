package com.example.foodorder.activity.useractivity.adapter.homever;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.useractivity.adapter.AdapterItemAction;
import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.model.Food;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeVerRecycle extends RecyclerView.Adapter<HomeVerHolder> {
    private List<Food> list;
    private List<Food> filteredList = new ArrayList<>();
    AdapterItemAction adapterItemAction;
    Context contentView;

    public HomeVerRecycle(List<Food> list, Context contentView) {
        this.list = list;
        this.contentView = contentView;
    }

    @NonNull
    @Override
    public HomeVerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list, parent, false);
        return new HomeVerHolder(view, adapterItemAction);
    }

    public void setAdapterItemAction(AdapterItemAction adapterItemAction) {
        this.adapterItemAction = adapterItemAction;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVerHolder holder, int position) {
        Food food = this.list.get(position);
        Picasso.get().load(food.getUrl()).into(holder.imageView);
        holder.textProductName.setText(food.getName());
        holder.textProductPrice.setText(food.getPrice() + "00 VND");
        holder.textProductType.setText(food.getType());
        holder.button.setOnClickListener(v -> {
                    this.adapterItemAction.onBTNClick(position);
                    if (holder.button.getVisibility() == View.INVISIBLE) {
                        holder.button.setVisibility(View.VISIBLE);
                        holder.button.startAnimation(AnimationUtils.loadAnimation(contentView, R.anim.add_to_cart_appear));
                    }
                    else {
                        holder.button.startAnimation(AnimationUtils.loadAnimation(contentView, R.anim.add_to_cart_disappear));
                        holder.button.setVisibility(View.INVISIBLE);
                    }
                }

        );

    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public void filterByNewtext(String text) {
        this.filteredList = this.list.stream().sorted(Comparator.comparingInt(f -> computeLevenshteinDistance(f.getName(), text))).collect(Collectors.toList());
        this.list = this.filteredList;
        notifyDataSetChanged();
    }

    public void filterByType(String type) {
        this.list = this.list.stream().sorted(Comparator.comparing(food -> !food.getType().equals(type))).collect(Collectors.toList());
        notifyDataSetChanged();

    }

    public static int computeLevenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                    );
                }
            }
        }

        return dp[len1][len2];
    }
}
