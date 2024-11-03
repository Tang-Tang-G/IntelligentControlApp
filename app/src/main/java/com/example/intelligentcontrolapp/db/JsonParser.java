package com.example.intelligentcontrolapp.db;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public List<House> parseJsonData(JSONObject data) {
        List<House> houseList = new ArrayList<>();
        try {
            JSONArray housesDevices = data.getJSONArray("houses_devices");
            for (int i = 0; i < housesDevices.length(); i++) {
                JSONObject house = housesDevices.getJSONObject(i);
                String houseName = house.getString("house_name");
                Log.e("HouseName","houseName:"+houseName);
                House newHouse = new House(houseName);  // 创建新房屋对象
                JSONArray areasDevices = house.getJSONArray("areas_devices");
                for (int j = 0; j < areasDevices.length(); j++) {
                    JSONObject area = areasDevices.getJSONObject(j);
                    String areaName = area.getString("area_name");
                    Area newArea = new Area(areaName);  // 创建新区域对象
                    JSONArray devices = area.getJSONArray("devices");
                    for (int k = 0; k < devices.length(); k++) {
                        JSONObject device = devices.getJSONObject(k);
                        String deviceName = device.getString("device_name");

                        String deviceType = device.getJSONObject("device_type").getString("type_name");
                        // 创建设备对象并添加到区域
                        Device deviceObj = new Device(deviceName, deviceType);
                        newArea.addDevice(deviceObj);
                    }
                    // 将区域添加到房屋
                    newHouse.addArea(newArea);
                }
                // 将房屋添加到房屋列表
                houseList.add(newHouse);
            }
        } catch (JSONException e) {
            Log.e("JsonParser", "JSON parsing error: " + e.getMessage());
        }
        return houseList;
    }
}
