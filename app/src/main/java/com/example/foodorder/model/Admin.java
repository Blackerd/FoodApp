package com.example.foodappproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Admin  implements Serializable {
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

    public Admin() {
    }

    public Admin(String id, String userAccount, String userPassword, String userName, String email, String number) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.email = email;
        this.number = number;
    }

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
    //    public void setAndHasId(String id) {
//        if (this.getId().equals("")) {
//            this.setId(id);
//        }
//    }
}
