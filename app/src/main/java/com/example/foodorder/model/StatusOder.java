package com.example.foodorder.model;

import java.io.Serializable;

public class StatusOder implements Serializable {
  private  String idOwner ;
    private BankAccount usedBankAccount;
    private Food food;
    private String status;

    public StatusOder(String idOwner,BankAccount usedBankAccount, Food food, String status) {
        this.idOwner = idOwner;
        this.usedBankAccount = usedBankAccount;
        this.food = food;
        this.status = status;
    }

    public StatusOder() {
    }

    public BankAccount getUsedBankAccount() {
        return usedBankAccount;
    }

    public void setUsedBankAccount(BankAccount usedBankAccount) {
        this.usedBankAccount = usedBankAccount;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
