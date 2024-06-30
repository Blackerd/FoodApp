package com.example.foodorder.retrofit.admin;


import com.example.foodorder.retrofit.ApiUntils;
import com.example.foodorder.retrofit.user.RetrofitUserClient;

public class AdminApiUtils {
    public static AdminSerivesInterfaces getServices(){
        return  RetrofitUserClient.getClient(ApiUntils.BASE_URL).create(AdminSerivesInterfaces.class);
    }
}
