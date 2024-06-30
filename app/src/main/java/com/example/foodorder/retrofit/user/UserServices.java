package com.example.foodorder.retrofit.user;

import android.util.Log;

import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServices {
    private UserServicesInterface userServicesInterface;

    public UserServices() {
        this.userServicesInterface = UserApiUtils.getServices();
    }

    public void login(String username, String userpass, GetOnDataListener onDataListener) {
        this.userServicesInterface.login(username, userpass).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User user = response.body();
                    if (user != null) {
                        onDataListener.onSuccess(user);
                    }
                } else {
                    int code = response.code();
                    onDataListener.onSuccess(code);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t);
                onDataListener.onFailure(t);
            }
        });
    }

    public void signup(User user, GetOnDataListener onDataListener) {
        this.userServicesInterface.signup(user.getUserAccount(), user.getUserPassword(), user.getUserName(), user.getEmail(), user.getNumber()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int code = response.code();
                    onDataListener.onSuccess(code);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                onDataListener.onFailure(t);
            }
        });
    }

    public void update(User user, GetOnDataListener onDataListener) {
        this.userServicesInterface.update(user).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d("code", "onResponse: " + response.body() + "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void order(String id, int total, Map<String, Object> listorder, GetOnDataListener onDataListener) {
        this.userServicesInterface.order(listorder, id, total).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    onDataListener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                onDataListener.onFailure(t);
            }
        });

    }


}
