package com.example.intelligentcontrolapp.db;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_token", token);
        editor.apply(); // 使用 apply() 以异步方式保存
    }

    public boolean isEmptyToken() {
        String token = getToken();
        return token == null || token.isEmpty();
    }

    public String getToken() {
        return sharedPreferences.getString("user_token", null); // 默认返回 null
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_token");
        editor.remove("user_name");
        editor.apply();
    }
    public void saveUserInfo(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", username);
        editor.apply(); // 使用 apply() 以异步方式保存
    }
    public String getUserName() {
        return sharedPreferences.getString("user_name", null); // 默认返回 null
    }
}
