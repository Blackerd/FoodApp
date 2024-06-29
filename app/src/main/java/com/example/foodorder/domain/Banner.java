package com.example.foodorder.domain;

public class Banner {
    private String image;

    public Banner() {
        // Constructor mặc định cần thiết cho Firebase
    }

    public Banner(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
