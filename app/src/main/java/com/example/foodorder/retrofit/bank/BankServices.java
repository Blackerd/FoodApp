package com.example.foodorder.retrofit.bank;

import com.example.foodorder.model.GetOnDataListener;
import com.example.foodorder.model.BankAccount;
import com.example.foodorder.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankServices {
    private BankServicesInterface bankServicesInterface;
    public BankServices (){
        this.bankServicesInterface = BankApiUntils.getServices();
    }
    public void getBanks(User user, GetOnDataListener onDataListener) {
        this.bankServicesInterface.getBankByID(user.getId()).enqueue(new Callback<List<BankAccount>>() {
            @Override
            public void onResponse(Call<List<BankAccount>> call, Response<List<BankAccount>> response) {
                if (response.isSuccessful()) {
                    List<BankAccount> list = response.body();
                    onDataListener.onSuccess(list);
                }
            }

            @Override
            public void onFailure(Call<List<BankAccount>> call, Throwable t) {
                onDataListener.onFailure(t);
            }
        });
    }
}
