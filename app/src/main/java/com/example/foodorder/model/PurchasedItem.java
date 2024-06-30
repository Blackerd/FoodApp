package com.example.foodorder.model;

import java.io.Serializable;

public class PurchasedItem implements Serializable {
    private Food purchasedItem;
    private BankAccount bankAccount;
    private String idOwner;

    public PurchasedItem(String idOwner,Food purchasedItem, BankAccount bankAccount) {
        this.idOwner = idOwner;
        this.purchasedItem = purchasedItem;
        this.bankAccount = bankAccount;
    }

    public PurchasedItem() {
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public Food getPurchasedItem() {
        return purchasedItem;
    }

    public void setPurchasedItem(Food purchasedItem) {
        this.purchasedItem = purchasedItem;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
