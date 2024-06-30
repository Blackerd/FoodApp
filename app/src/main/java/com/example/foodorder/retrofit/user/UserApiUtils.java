package com.example.foodorder.retrofit.user;

import com.example.foodorder.retrofit.ApiUntils;

public class UserApiUtils {
    public static UserServicesInterface getServices(){
        return RetrofitUserClient.getClient(ApiUntils.BASE_URL).create(UserServicesInterface.class);
    }
}
