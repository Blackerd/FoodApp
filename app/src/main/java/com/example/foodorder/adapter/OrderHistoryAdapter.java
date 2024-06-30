package com.example.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.domain.Orders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<Orders> ordersList;
    private Context context;

    public OrderHistoryAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders order = ordersList.get(position);

        // Populate views with order data
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.userIdTextView.setText("User ID: " + order.getUserId());
        holder.itemTotalTextView.setText("Item Total: $" + order.getItemTotal());
        holder.taxTextView.setText("Tax: $" + order.getTax());
        holder.deliveryFeeTextView.setText("Delivery Fee: $" + order.getDeliveryFee());
        holder.totalTextView.setText("Total: $" + order.getTotal());
        holder.addressTextView.setText("Address: " + order.getAddress());
        holder.statusTextView.setText("Status: " + Orders.getStatusString(order.getStatus()));
        holder.userNameTextView.setText("User Name: " + order.getUserName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String orderDate = dateFormat.format(order.getOrderDate());
        holder.orderDateTextView.setText("Order Date: " + orderDate);

        // Hide the user ID TextView since it's not necessary in user view
        holder.userIdTextView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, orderIdTextView, userIdTextView, itemTotalTextView, taxTextView, deliveryFeeTextView, totalTextView, addressTextView, orderDateTextView, statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTxt);
            orderIdTextView = itemView.findViewById(R.id.orderIdTxt);
            userIdTextView = itemView.findViewById(R.id.userIdTxt);
            itemTotalTextView = itemView.findViewById(R.id.itemTotalTxt);
            taxTextView = itemView.findViewById(R.id.taxTxt);
            deliveryFeeTextView = itemView.findViewById(R.id.deliveryFeeTxt);
            totalTextView = itemView.findViewById(R.id.totalTxt);
            addressTextView = itemView.findViewById(R.id.addressTxt);
            orderDateTextView = itemView.findViewById(R.id.orderDateTxt);
            statusTextView = itemView.findViewById(R.id.statusTxt);
        }
    }
}
