package com.example.foodorder.retrofit.user;


import com.example.foodorder.model.Food;
import com.example.foodorder.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserServicesInterface {
    @POST("user/check")
    Call<User> login(@Query("useremail") String username, @Query("userpass") String userpass);
    @POST("user/sign")
    Call<Integer> signup(@Query("useraccount") String useraccount, @Query("userpass") String userpass ,@Query("username") String username , @Query("email") String email,@Query("number") String number);
    @POST("user/update")
    Call<Integer> update(@Body User user);
    @POST("user/order")
    Call<Integer> order(@Body Map<String, Object> orders , @Query("id") String id , @Query("total") int total);

}
