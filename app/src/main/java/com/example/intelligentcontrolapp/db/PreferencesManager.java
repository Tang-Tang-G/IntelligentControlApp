package com.example.intelligentcontrolapp.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.intelligentcontrolapp.network.CustomCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static final JsonParser jsonParser = new JsonParser();

    private final SharedPreferences sharedPreferences;
    private List<House> houses = null;

    public List<House> getHouses() {
        if (houses == null) {
            return new ArrayList<>();
        }
        return houses;
    }

    public void setHouses(List<House> newHouses) {
        houses = newHouses;
    }

    public List<Area> getAreas(String house_name) {
        for (House house : houses) {
            if (house.getName().equals(house_name)) {
                return house.getAreas();
            }
        }
        return new ArrayList<>();
    }

    public JsonParser getJsonParser(){
        return jsonParser;
    }

    private void fetchData(Context context) {
        NetworkUtils.getDataInfo(context, new CustomCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject dataInfo) {
                Log.d(TAG, "Success datainfo");
                //获取家庭数据，区域数据，设备数据
                setHouses(jsonParser.parseJsonData(dataInfo));
            }
            @Override
            public void onError(String message) {
                Log.e(TAG, "Error datainfo:" + message);
            }
        });
    }

    public void fetchDevicesData(Context context) {
        if (!isEmptyToken() && (houses == null || houses.isEmpty())) {
            Log.d(this.getClass().getName(), "fetchData");
            fetchData(context);
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
