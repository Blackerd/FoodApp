package com.example.foodorder.retrofit.admin;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminSerivesInterfaces {
    @POST("admin/check")
    Call<com.example.foodappproject.model.Admin> login(@Query("email") String email, @Query("pass") String pass);
}
