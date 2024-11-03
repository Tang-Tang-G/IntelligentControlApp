package com.example.intelligentcontrolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.activities.AboutAppActivity;
import com.example.intelligentcontrolapp.activities.ChangePasswordActivity;
import com.example.intelligentcontrolapp.activities.ContactUsActivity;
import com.example.intelligentcontrolapp.activities.LoginActivity;
import com.example.intelligentcontrolapp.activities.MainActivity;
import com.example.intelligentcontrolapp.activities.PrivacyPolicyActivity;
import com.example.intelligentcontrolapp.activities.SystemNotificationActivity;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private List<Class<?>> targetActivities;
    private Button exit_login;
    private View rootview;
    private TextView login;
    private TextView tv_username, tv_nickname;
    private final List<RelativeLayout> relativeLayouts = new ArrayList<>();
    private static final long COLOR_CHANGE_DURATION = 100; // 颜色变化持续时间（毫秒)
    private ActivityResultLauncher<Intent> loginActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (MyApplication.getPreferencesManager().isEmptyToken()) {
            //未登录状态
            rootview = inflater.inflate(R.layout.fragment_my, container, false);
            login = rootview.findViewById(R.id.tv_login);
            //点击前往登录
            login.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            setupLoggedInView();
        }
        //初始化控件

        // 初始化目标活动列表
        targetActivities = new ArrayList<>();
        targetActivities.add(ChangePasswordActivity.class);
        targetActivities.add(ContactUsActivity.class);
        targetActivities.add(SystemNotificationActivity.class);
        targetActivities.add(PrivacyPolicyActivity.class);
        targetActivities.add(AboutAppActivity.class);

        // 初始化 RelativeLayout 列表并设置点击事件
        initRelativeLayouts();
        //点击退出登录
        exit_login = rootview.findViewById(R.id.bt_exit_login);
        if (exit_login != null) {
            exit_login.setOnClickListener(view -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("温馨提示")
                        .setMessage("确定要退出登录嘛？")
                        .setPositiveButton("确认", (dialogInterface, i) -> {
                            // 退出登录
                            MyApplication.getPreferencesManager().clearToken();
                            // 重置视图
                            setupLoggedOutView();
                            ((MainActivity) getActivity()).updateFragment(new MainFragment());

                        })
                        .setNegativeButton("取消", null)
                        .show();
            });
        }
        return rootview;
    }

    private void initRelativeLayouts() {
        for (int i = 1; i <= 5; i++) {
            int resId = getResources().getIdentifier("relative_layout_" + i, "id", getActivity().getPackageName());
            RelativeLayout layout = rootview.findViewById(resId);
            if (layout != null) {
                relativeLayouts.add(layout);
                int finalI = i - 1;
                layout.setOnClickListener(view -> {
                    changeColor(view);
                    navigateToNextActivity(finalI);
                });
            }
        }
    }

    private void setupLoggedInView() {
        rootview = getLayoutInflater().inflate(R.layout.fragment_my_logined, null);
        tv_username = rootview.findViewById(R.id.tv_username);
        tv_nickname = rootview.findViewById(R.id.tv_nickname);
        String username = MyApplication.getPreferencesManager().getUserName();
        tv_username.setText(username);
        initRelativeLayouts();  // 初始化 RelativeLayout 并设置点击事件
        ((MainActivity) getActivity()).updateFragment(this);
    }

    private void setupLoggedOutView() {
        rootview = getLayoutInflater().inflate(R.layout.fragment_my, null);
        login = rootview.findViewById(R.id.tv_login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginActivityResultLauncher.launch(intent);
        });
        ((MainActivity) getActivity()).updateFragment(new MainFragment());
    }

    private void navigateToNextActivity(int i) {
        Intent intent = new Intent(getActivity(), targetActivities.get(i)); // 替换为你的目标Activity
        startActivity(intent);
    }

    private void changeColor(View view) {
        // 设置新颜色
        view.setBackgroundColor(getResources().getColor(R.color.blue));
        // 使用Handler延迟恢复颜色
        new Handler().postDelayed(() -> {
            // 恢复原来的颜色
            view.setBackgroundColor(getResources().getColor(R.color.gray_white));
        }, COLOR_CHANGE_DURATION); // 延迟时间
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.getPreferencesManager().isEmptyToken()) {
            setupLoggedInView();
        } else {
            setupLoggedOutView();
        }
    }
}