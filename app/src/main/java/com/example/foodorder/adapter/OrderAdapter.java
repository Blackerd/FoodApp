// OrderAdapter.java
package com.example.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.domain.Orders;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Orders> ordersList;
    private OrderClickListener clickListener;
    private Orders selectedOrder;
    private Context context;


    public OrderAdapter(Context context, List<Orders> ordersList, OrderClickListener clickListener) {
        this.context = context;
        this.ordersList = ordersList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders order = ordersList.get(position);

        // Hiển thị thông tin đơn hàng lên giao diện
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

        // Kiểm tra trạng thái và ẩn/hiển thị các nút tương ứng
        switch (order.getStatus()) {
            case Orders.STATUS_PENDING:
                // Trạng thái chờ xác nhận: hiển thị tất cả các nút
                holder.acceptBtn.setVisibility(View.VISIBLE);
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.updateStatusBtn.setVisibility(View.GONE);
                break;
            case Orders.STATUS_CONFIRMED:
            case Orders.STATUS_DELIVERING:
                // Trạng thái đã xác nhận hoặc đang giao: chỉ hiển thị nút cập nhật
                holder.updateStatusBtn.setVisibility(View.VISIBLE);
                holder.acceptBtn.setVisibility(View.GONE);
                holder.cancelBtn.setVisibility(View.GONE);
                break;
            case Orders.STATUS_COMPLETED:
            case Orders.STATUS_CANCELLED:
                // Trạng thái đã thành công hoặc đã hủy: không hiển thị nút nào
                holder.updateStatusBtn.setVisibility(View.GONE);
                holder.acceptBtn.setVisibility(View.GONE);
                holder.cancelBtn.setVisibility(View.GONE);
                break;
        }

        // Xử lý khi nhấn nút xác nhận
        holder.acceptBtn.setOnClickListener(v -> {
            // Lưu đơn hàng được chọn
            selectedOrder = order;
            // Gọi phương thức onAcceptClick trong interface clickListener
            clickListener.onAcceptClick(order);
            // Cập nhật lại giao diện
            notifyDataSetChanged();
            // hiển thị nút cập nhật trạng thái
            holder.updateStatusBtn.setVisibility(View.VISIBLE);
            // ẩn đi nút xác nhận và hủy
            holder.acceptBtn.setVisibility(View.GONE);
            holder.cancelBtn.setVisibility(View.GONE);
        });

        holder.cancelBtn.setOnClickListener(v -> {
            // Lưu đơn hàng được chọn
            selectedOrder = order;
            // Gọi phương thức onCancelClick trong interface clickListener
            clickListener.onCancelClick(order);
            // Cập nhật lại giao diện
            notifyDataSetChanged();
            // hiển thị nút cập nhật trạng thái
            holder.updateStatusBtn.setVisibility(View.VISIBLE);
            // ẩn đi nút xác nhận và hủy
            holder.acceptBtn.setVisibility(View.GONE);
            holder.cancelBtn.setVisibility(View.GONE);
        });

        // Xử lý khi nhấn nút cập nhật trạng thái
        holder.updateStatusBtn.setOnClickListener(v -> {
            // Gọi phương thức onUpdateStatusClick trong interface clickListener
            clickListener.onUpdateStatusClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView,orderIdTextView, userIdTextView, itemTotalTextView, taxTextView, deliveryFeeTextView, totalTextView, addressTextView, orderDateTextView, statusTextView;
        private Button acceptBtn, cancelBtn , updateStatusBtn;

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
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            updateStatusBtn = itemView.findViewById(R.id.updateStatusBtn);
        }
    }

    // Interface để xử lý sự kiện click trên adapter
    public interface OrderClickListener {
        // Phương thức xử lý khi nhấn nút xác nhận
        void onAcceptClick(Orders order);

        // Phương thức xử lý khi nhấn nút hủy
        void onCancelClick(Orders order);

        // Phương thức xử lý khi nhấn nút cập nhật trạng thái
        void onUpdateStatusClick(Orders order);
    }
}
