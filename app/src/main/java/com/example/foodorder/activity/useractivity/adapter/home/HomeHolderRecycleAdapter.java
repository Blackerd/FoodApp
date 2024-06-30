package com.example.foodorder.activity.useractivity.adapter.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.model.Food;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeHolderRecycleAdapter extends RecyclerView.Adapter<HomeItemRecycle> {
    public interface OnDataRecycleAdapter {
        public void onClickBTN(int potision);

        public void onItemClick(int position);
    }

    public void setOnDataRecycleAdapter(OnDataRecycleAdapter onDataRecycleAdapter) {
        this.onDataRecycleAdapter = onDataRecycleAdapter;
    }


    public List<Food> list;
    private List<Food> filteredList = new ArrayList<>();
    public OnDataRecycleAdapter onDataRecycleAdapter;

    public HomeHolderRecycleAdapter(List<Food> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HomeItemRecycle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new HomeItemRecycle(view, onDataRecycleAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemRecycle holder, int position) {
        Food item = this.list.get(position);
        int index = position;

        Picasso.get().load(item.getUrl()).into(holder.imageView);
        holder.textProductName.setText(item.getName());
        holder.textProductPrice.setText(item.getPrice() + "00 VND");
        holder.textProductType.setText(item.getType());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataRecycleAdapter.onClickBTN(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }


    public void filterByName(String query) {
        this.filteredList = this.list.stream().sorted(Comparator.comparingInt(f -> computeLevenshteinDistance(f.getName(), query))).collect(Collectors.toList());
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
