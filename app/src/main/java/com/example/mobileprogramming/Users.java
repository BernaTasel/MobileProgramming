package com.example.mobileprogramming;

public class Users {
    private String username;
    private String password;

    public Users() {
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUserrname() {
        return username;
    }

    public void setUsername(String userrname) {
        this.username = userrname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
