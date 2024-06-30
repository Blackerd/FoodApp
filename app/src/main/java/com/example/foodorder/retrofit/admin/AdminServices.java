package com.example.foodorder.retrofit.admin;


import android.util.Log;

import com.example.foodorder.model.GetOnDataListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminServices {
private AdminSerivesInterfaces adminSerivesInterfaces;

    public AdminServices() {
        this.adminSerivesInterfaces = AdminApiUtils.getServices();
    }
    public void login(String email, String pass, GetOnDataListener onDataListener) {
        this.adminSerivesInterfaces.login(email, pass).enqueue(new Callback<com.example.foodappproject.model.Admin>() {
            @Override
            public void onResponse(Call<com.example.foodappproject.model.Admin> call, Response<com.example.foodappproject.model.Admin> response) {

                if (response.isSuccessful()) {
                    Log.d("ADMIN", "onResponse: "+response.body());
                    com.example.foodappproject.model.Admin admin = response.body();
                    if (admin != null) {
                        onDataListener.onSuccess(admin);
                    }
                } else {
                    int code = response.code();
                    onDataListener.onSuccess(code);
                }
            }

            @Override
            public void onFailure(Call<com.example.foodappproject.model.Admin> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t);
                onDataListener.onFailure(t);
            }
        });
    }



}
