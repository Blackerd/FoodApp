package com.example.foodorder.domain;

public class Users {
    private int id;
    private String email;
    private String password;
    private boolean admin;
    private String name;


    public Users() {
    }

    public Users(String name, String email, boolean admin, String password) {
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
