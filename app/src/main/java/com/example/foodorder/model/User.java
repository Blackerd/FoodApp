package com.example.foodorder.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User  implements Serializable {
    private static final String NODE_NAME_FIREBASE = "USERS";
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("userAccount")
    @Expose
    private String userAccount;

    @SerializedName("userPassword")
    @Expose
    private String userPassword;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("number")
    @Expose
    private String number;


    private List<BankAccount> listBanks;
    private List<PurchasedItem> purchasedHistory;
    private List<StatusOder> statusOders;

    public User() {

    }



    public User(String idUser, String userAccount, String userPassword, String userName, String email, String number) {
        this.id = idUser;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.email = email;
        this.number = number;
        this.listBanks = new ArrayList<>();
        this.purchasedHistory = new ArrayList<>();
        this.statusOders = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<BankAccount> getListBanks() {
        return listBanks;
    }

    public void setListBanks(List<BankAccount> listBanks) {
        this.listBanks = listBanks;
    }

    public List<PurchasedItem> getPurchasedHistory() {
        return purchasedHistory;
    }

    public void setPurchasedHistory(List<PurchasedItem> purchasedHistory) {
        this.purchasedHistory = purchasedHistory;
    }

    public List<StatusOder> getStatusOders() {
        return statusOders;
    }

    public void setStatusOders(List<StatusOder> statusOders) {
        this.statusOders = statusOders;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                ", listBanks=" + listBanks +
                ", purchasedHistory=" + purchasedHistory +
                ", statusOders=" + statusOders +
                '}';
    }
}
