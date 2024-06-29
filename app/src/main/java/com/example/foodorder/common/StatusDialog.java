package com.example.foodorder.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.foodorder.domain.Orders;

public class StatusDialog {

    public interface StatusDialogListener {
        void onUpdateStatus(int status);
    }

    public static void showStatusOptionsDialog(Context context, StatusDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái mới");

        String[] statusOptions = {"Pending", "Confirmed", "Completed", "Cancelled"};

        builder.setItems(statusOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newStatus = getStatusFromOption(which);
                // Gọi phương thức onUpdateStatus của listener để thông báo trạng thái mới đã được chọn
                listener.onUpdateStatus(newStatus);
            }
        });

        builder.show();
    }

    // Lấy trạng thái mới dựa trên lựa chọn của người dùng
    private static int getStatusFromOption(int optionIndex) {
        switch (optionIndex) {
            case 0:
                return Orders.STATUS_PENDING;
            case 1:
                return Orders.STATUS_CONFIRMED;
            case 2:
                return Orders.STATUS_COMPLETED;
            case 3:
                return Orders.STATUS_CANCELLED;
            default:
                return Orders.STATUS_PENDING; // Mặc định là Pending
        }
    }
}
