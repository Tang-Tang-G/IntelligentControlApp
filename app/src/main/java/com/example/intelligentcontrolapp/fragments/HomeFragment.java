package com.example.intelligentcontrolapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.intelligentcontrolapp.Mycomponent.wifidevicepair;
import com.example.intelligentcontrolapp.PairDevice.Pair;
import com.example.intelligentcontrolapp.PairDevice.WiFiDevicePairing;
import com.example.intelligentcontrolapp.R;

public class HomeFragment extends Fragment {
    private Button addFamily;
    private LinearLayout familyContainer;
    private View rootview;
    private ImageView device;
    private String ssid, passwd, host;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_home, container, false);

        // 初始化控件
        addFamily = rootview.findViewById(R.id.addFamilyButton);
        familyContainer = rootview.findViewById(R.id.familyContainer); // 确保已初始化 familyContainer

        device = rootview.findViewById(R.id.ib_device_pair);

        // 设置点击事件
        addFamily.setOnClickListener(view -> {
            // 创建新的用户视图
            addFamily();
        });

        device.setOnClickListener(view -> {
            //弹出弹窗在里面输入ssid，passwd，host  用来连接设备wife
            showWiFiDialog();
        });

        return rootview;
    }

    private void showWiFiDialog() {
        wifidevicepair dialog = new wifidevicepair(getContext(), getActivity());
        dialog.setOnDevicePairListener(new wifidevicepair.OnDevicePairListener() {
            @Override
            public void onPair(String ssid, String passwd, String host) {
                Pair.Message message = new Pair.Message(ssid, passwd, host);
                WiFiDevicePairing devicePairing = new WiFiDevicePairing(message);
                devicePairing.new WiFiDevicePairingTask().execute();
            }
        });
        dialog.show();
    }

    private void addFamily() {
        // 使用独立的方法创建新的用户卡片
        CardView newUserCard = createNewUserCard();

        // 将新用户视图添加到 addFamilyButton 左边
        int addUserButtonIndex = familyContainer.indexOfChild(rootview.findViewById(R.id.addFamilyCard));
        familyContainer.addView(newUserCard, addUserButtonIndex);
    }

    private CardView createNewUserCard() {
        CardView newUserCard = new CardView(getContext());
        newUserCard.setRadius(dpToPx(25)); // 使用 dp 转 px

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(40), dpToPx(40));
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0); // 设置边距
        newUserCard.setLayoutParams(params);

        // 设置用户图标
        ImageView newUserImage = new ImageView(getContext());
        newUserImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        newUserImage.setImageResource(R.drawable.my); // 设置用户图标
        newUserCard.addView(newUserImage);

        return newUserCard;
    }

    // dp 转 px 方法
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


}
