package com.example.foodorder.retrofit.bank;

import com.example.foodorder.retrofit.ApiUntils;

public class BankApiUntils {
    public static BankServicesInterface getServices() {
        return RetrofitBankClient.getClient(ApiUntils.BASE_URL).create(BankServicesInterface.class);
    }


}
