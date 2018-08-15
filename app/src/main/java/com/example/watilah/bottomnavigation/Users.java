package com.example.watilah.bottomnavigation;

public class Users {

    private String name;
    private String email;
    private String usertype;

    public Users() {
    }

    public Users(String name, String email, String usertype) {
        this.name = name;
        this.email = email;
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}