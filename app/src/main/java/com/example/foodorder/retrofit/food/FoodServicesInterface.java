package com.example.foodorder.retrofit.food;

import com.example.foodorder.model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodServicesInterface {
    @GET("food/food")
    Call<List<Food>> getListFoods();
}
