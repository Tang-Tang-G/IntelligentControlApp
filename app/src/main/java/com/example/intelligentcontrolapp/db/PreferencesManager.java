package com.example.intelligentcontrolapp.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.intelligentcontrolapp.network.CustomCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;

import org.json.JSONObject;

import java.util.List;

public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static final JsonParser jsonParser = new JsonParser();

    private final SharedPreferences sharedPreferences;
    private List<House> houses = null;

    private void fetchData(Context context, CustomCallback<List<House>> callback) {
        NetworkUtils.getDataInfo(context, new CustomCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject dataInfo) {
                Log.d(TAG, "Success datainfo");
                //获取家庭数据，区域数据，设备数据
                houses = jsonParser.parseJsonData(dataInfo);
                callback.onSuccess(houses);
            }
            @Override
            public void onError(String message) {
                Log.e(TAG, "Error datainfo:" + message);
                callback.onError(message);
            }
        });
    }

    public void fetchDevicesData(Context context, CustomCallback<List<House>> callback) {
        if (houses == null || houses.isEmpty()) {
            if (!isEmptyToken()) {
                Log.d(this.getClass().getName(), "fetchData");
                fetchData(context, callback);
            }
        } else {
            callback.onSuccess(houses);
        }
    }

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
