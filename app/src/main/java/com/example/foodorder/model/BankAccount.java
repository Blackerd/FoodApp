package com.example.foodorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankAccount implements Serializable {
    @SerializedName("idBank")
    @Expose
    String idBank;
    @SerializedName("idOwner")
    @Expose
    private String idOwner;
    @SerializedName("nameBankAccount")
    @Expose
    private String nameBankAccount;
    @SerializedName("accountbalance")
    @Expose
    private long accountbalance;

    public BankAccount() {

    }

    public BankAccount(String idBank, String idOwner, String nameBankAccount, long accountbalance) {
        this.idBank = idBank;
        this.idOwner = idOwner;
        this.nameBankAccount = nameBankAccount;
        this.accountbalance = accountbalance;
    }

    public String getIdBank() {
        return idBank;
    }

    public void setIdBank(String idBank) {
        this.idBank = idBank;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getNameBankAccount() {
        return nameBankAccount;
    }

    public void setNameBankAccount(String nameBankAccount) {
        this.nameBankAccount = nameBankAccount;
    }

    public long getAccountbalance() {
        return accountbalance;
    }

    public void setAccountbalance(long accountbalance) {
        this.accountbalance = accountbalance;
    }

    public void isHasIdAndSet(String id) {
        if (this.getIdBank().equals("")) this.setIdBank(id);

    }
}
