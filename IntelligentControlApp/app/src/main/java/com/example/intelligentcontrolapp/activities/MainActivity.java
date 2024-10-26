package com.example.intelligentcontrolapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.fragments.HomeFragment;
import com.example.intelligentcontrolapp.fragments.MainFragment;
import com.example.intelligentcontrolapp.fragments.MyFragment;
import com.example.intelligentcontrolapp.fragments.SceneFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Fragment> list;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new HomeFragment());
        list.add(new SceneFragment());
        list.add(new MyFragment());

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
        Log.d("update","1jiaoslnsblsbks;vs");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }
}