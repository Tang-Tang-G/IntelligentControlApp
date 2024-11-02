package com.example.intelligentcontrolapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.db.House;
import com.example.intelligentcontrolapp.db.JsonParser;
import com.example.intelligentcontrolapp.fragments.HomeFragment;
import com.example.intelligentcontrolapp.fragments.MainFragment;
import com.example.intelligentcontrolapp.fragments.MyFragment;
import com.example.intelligentcontrolapp.fragments.SceneFragment;
import com.example.intelligentcontrolapp.network.DataCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Fragment> list;
    private BottomNavigationView bottomNavigationView;
    private JsonParser jsonParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new HomeFragment());
        list.add(new SceneFragment());
        list.add(new MyFragment());

        Log.e("token",MyApplication.getPreferencesManager().getToken());

        if(!MyApplication.getPreferencesManager().isEmptyToken())
        {
            NetworkUtils.getDataInfo(this, new DataCallback() {
                @Override
                public void onSuccess(JSONObject DataInfo) {
                    Log.e("DataInfo","Success datainfo");
                jsonParser = new JsonParser();
                //获取家庭数据，区域数据，设备数据
                MyApplication.getInstance().setHouses(jsonParser.parseJsonData(DataInfo));

                }
                @Override
                public void onError(String errorMessage) {
                    Log.e("DataInfo","Error datainfo:"+errorMessage);
                }
            });
        }
        // 获取传递的意图
        Intent intent = getIntent();
        String fragmentToShow = intent.getStringExtra("show_fragment");

        showFragment(list.get(0));
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.menu_main) {
                    showFragment(list.get(0));
                } else if (i == R.id.menu_home) {
                    showFragment(list.get(1));
                } else if (i == R.id.menu_scene) {
                    showFragment(list.get(2));
                } else if (i == R.id.menu_my) {
                    showFragment(list.get(3));
                }
                return true;
            }
        });

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    public void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }
}