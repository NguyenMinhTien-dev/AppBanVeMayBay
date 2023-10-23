package com.example.appbanvemaybay;

import android.app.Application;

public class StatusLogin extends Application {
    public static boolean login = false;
    public static String user = "";
    public static String name = "";

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
