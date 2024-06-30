package com.example.foodorder.retrofit.food;

import com.example.foodorder.retrofit.ApiUntils;

public class FoodApiUtils {
    public static FoodServicesInterface getServices(){
        return  RetrofitFoodClient.getClient(ApiUntils.BASE_URL).create(FoodServicesInterface.class);
    }
}
