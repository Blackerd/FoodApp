package com.example.foodorder.model;


import java.io.Serializable;

public abstract class Infor implements Serializable {
    private String id, userAccount, userPassword, userName, email, number;

    public Infor() {
    }


    public Infor(String id, String userAccount, String userPassword, String userName, String email, String number) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.email = email;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Infor{" +
                "id='" + id + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                '}';
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

    public void setAndHasId(String id) {
        if (this.getId().equals("")) {
            this.setId(id);
        }
    }
}
