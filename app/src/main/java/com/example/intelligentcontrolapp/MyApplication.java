package com.example.intelligentcontrolapp;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.intelligentcontrolapp.db.Area;
import com.example.intelligentcontrolapp.db.House;
import com.example.intelligentcontrolapp.db.PreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static PreferencesManager preferencesManager;

    private List<House> houses = new ArrayList<>();
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
    public void addComponent(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null);

        bottomSheetView.findViewById(R.id.add_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理添加设备逻辑
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.add_scene).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理添加场景逻辑
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.add_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理添加区域逻辑
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理添加家庭成员逻辑
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    //暂时不进行密码校验
    public static boolean isPasswordStrong(String password) {
        // 最少8位，包含大写字母、小写字母、数字和符号
//        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
//        return password.matches(regex);
    return true;
    }

    public static PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public List<House> getHouses() {
        return houses;
    }

    public void setHouses(List<House> newHouses) {
        houses.clear(); // 清空当前列表
        houses.addAll(newHouses); // 添加新数据
    }
    public List<Area> getAreas(String house_name) {
        for (House house : houses) {
            if (house.getName().equals(house_name)) {
                return house.getAreas();
            }
        }
        return new ArrayList<>();
    }
}
