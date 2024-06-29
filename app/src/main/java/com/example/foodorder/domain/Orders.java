package com.example.foodorder.domain;

import java.util.Date;
import java.util.List;

public class Orders {
    // Các trạng thái của đơn hàng
    public static final int STATUS_PENDING = 0; // Chờ xác nhận
    public static final int STATUS_CONFIRMED = 1; // Đã xác nhận
    public static final int STATUS_DELIVERING = 2; // Đang giao
    public static final int STATUS_COMPLETED = 3; // Thành công
    public static final int STATUS_CANCELLED = 4; // Đã hủy

    // Các trạng thái của đơn hàng dưới dạng chuỗi
    public static final String STATUS_PENDING_STRING = "Chờ xác nhận";
    public static final String STATUS_CONFIRMED_STRING = "Đã xác nhận";
    public static final String STATUS_DELIVERING_STRING = "Đang giao";
    public static final String STATUS_COMPLETED_STRING = "Thành công";
    public static final String STATUS_CANCELLED_STRING = "Đã hủy";

    private int orderId;
    private int userId;
    private List<String> items;
    private double itemTotal;
    private double tax;
    private double deliveryFee;
    private double total;
    private String address;
    private Date orderDate;
    private int statusId;
    private String userName;
    private String statusString;

    public Orders() {
        // Đặt giá trị mặc định cho status là 0 (chờ xác nhận)
        this.statusId = 0;
    }

    public Orders(String userName,int orderId, int userId, List<String> items, double itemTotal, double tax, double deliveryFee, double total, String address, Date orderDate) {
        this.userName = userName;
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.itemTotal = itemTotal;
        this.tax = tax;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.address = address;
        this.orderDate = orderDate;
        // Đặt giá trị mặc định cho status là 0 (chờ xác nhận)
        this.statusId = 0;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public void setStatus(int status) {
        this.statusId = status;
        this.statusString = getStatusString(status);
    }

    public static String getStatusString(int status) {
        switch (status) {
            case STATUS_PENDING:
                return STATUS_PENDING_STRING;
            case STATUS_CONFIRMED:
                return STATUS_CONFIRMED_STRING;
            case STATUS_DELIVERING:
                return STATUS_DELIVERING_STRING;
            case STATUS_COMPLETED:
                return STATUS_COMPLETED_STRING;
            case STATUS_CANCELLED:
                return STATUS_CANCELLED_STRING;
            default:
                return "Unknown";
        }
    }

    public String getStatusString() {
        return statusString;
    }

    public int getStatus() {
        return statusId;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }
}
