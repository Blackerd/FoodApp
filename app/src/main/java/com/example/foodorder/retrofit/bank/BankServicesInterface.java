package com.example.foodorder.retrofit.bank;

import com.example.foodorder.model.BankAccount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BankServicesInterface {
    @GET("bank/bank")
    Call<List<BankAccount>> getBankByID(@Query("id") String id);

}
