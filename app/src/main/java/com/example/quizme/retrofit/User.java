package com.example.quizme.retrofit;

public class User {
    private String Login,Password;
    private long Coins;

    public String getLogin() {
        return Login;
    }

    public void setLogin(String name) {
        this.Login = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String pass) {
        this.Password = pass;
    }


    public long getCoins() {
        return Coins;
    }

    public void setCoins(long coins) {
        this.Coins = coins;
    }

}
