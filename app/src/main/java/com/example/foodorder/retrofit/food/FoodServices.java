package com.example.foodorder.retrofit.food;

import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodServices {
    private FoodServicesInterface foodServices;

    public FoodServices() {
        this.foodServices = FoodApiUtils.getServices();
    }

    public void addFood(Food food, GetOnDataListener onDataListener) {

    }

    public void getFood(GetOnDataListener onDataListener) {
        this.foodServices.getListFoods().enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food>  foods = (List<Food>) response.body();
                    onDataListener.onSuccess(foods);
//
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                        onDataListener.onFailure(t);
            }
        });
    }
}
