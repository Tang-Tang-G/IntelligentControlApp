package com.example.intelligentcontrolapp;

import android.app.Application;
import android.view.View;

import com.example.intelligentcontrolapp.db.PreferencesManager;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 在这里进行全局初始化，例如：
        preferencesManager = new PreferencesManager(this);


        initNetworkLibrary();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initNetworkLibrary() {
        // 进行网络库的初始化
    }

    public static boolean isPasswordStrong(String password) {
        // 最少8位，包含大写字母、小写字母、数字和符号
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }

    public static PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }
}
