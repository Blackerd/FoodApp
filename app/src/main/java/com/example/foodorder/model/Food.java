package com.example.foodorder.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable{

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("price")
    @Expose
    public double price;

    @SerializedName("quantity")
    @Expose
    public int quantity =0 ;

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;

    public Food(String path, String name, double price, String type ,String url) {
        this.path = path;
        this.name = name;
        this.price = price;
        this.type = type;
        this.url = url;

    }

    public Food() {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Food{" +
                "Path=" + path +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }


}
