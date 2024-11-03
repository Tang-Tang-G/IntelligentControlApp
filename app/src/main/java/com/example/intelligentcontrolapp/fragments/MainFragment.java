package com.example.intelligentcontrolapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.intelligentcontrolapp.Adapter.HomeListAdapter;
import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.db.Area;
import com.example.intelligentcontrolapp.db.Device;
import com.example.intelligentcontrolapp.db.House;
import com.example.intelligentcontrolapp.network.CustomCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MainFragment extends Fragment {
    private View rootView;
    private LinearLayout deviceContainer;
    private HomeListAdapter homeListAdapter;
    private Spinner homeList;

    private List<House> houses; // use it in async callback
    private List<String> labelNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.getClass().getName(), "onCreateView");

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ImageButton addButton = rootView.findViewById(R.id.ib_add_component);
        homeList = rootView.findViewById(R.id.spinner_home_list);
        deviceContainer = rootView.findViewById(R.id.device_container);
        labelNames = new ArrayList<>();

        Log.d(getTag(), "FetchData in MainFragment");
        // 从 MyApplication 获取 houses 列表
        MyApplication.getPreferencesManager().fetchDevicesData(getContext(), new CustomCallback<List<House>>() {
            @Override
            public void onSuccess(List<House> data) {
                houses = data;
                Log.d(getTag(), "FetchData Successfully");

                for (House house : data) {
                    labelNames.add(house.getName());
                }

                if (!data.isEmpty()) {
                    FragmentActivity activity = getActivity();
                    if (activity == null) {
                        Log.e(this.getClass().getName(), "No activity");
                    } else {
                        activity.runOnUiThread(() -> showCurrentHouseDevices(data, data.get(0).getName()));
                    }
                }
            }

            @Override
            public void onError(String message) {
                Log.d(getTag(), "FetchDeviceData and show Error " + message);
            }
        });


        //添加按钮。
        addButton.setOnClickListener(view -> MyApplication.getInstance().addComponent(getContext()));
        //家庭列表设计
        homeListAdapter = new HomeListAdapter(getContext(), labelNames.toArray(new String[0]));
        //将适配器与下拉列表框关联起来
        homeList.setAdapter(homeListAdapter);
        homeListAdapter.setOnHomeSelectedListener((home, position) -> homeList.setSelection(position));
        //某个家庭列表被点击时，设置 HomeListAdapter 的选中位置，更新设备列表，并且刷新这个fragment。
        homeList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String result = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        homeListAdapter.setSelectedPosition(i);
                        // 在这里更新设备列表
                        FragmentActivity activity = getActivity();
                        if (activity == null) {
                            Log.e(this.getClass().getName(), "No activity");
                        } else {
                            activity.runOnUiThread(() -> showCurrentHouseDevices(houses, result));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        labelNames.add("家庭管理"); // 添加 "Manage Homes" 选项
        return rootView;
    }

    private void showCurrentHouseDevices(List<House> houses, String home) {
        Log.d(this.getTag(), "updateDeviceList");

        List<Device> devices = houses
                .stream()
                .map(House::getAreas)
                .flatMap(Collection::stream)
                .map(Area::getDevices)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        // 清空所有的设备列表
        deviceContainer.removeAllViews();
        // 添加设备列表
        if (!devices.isEmpty()) {
            for (Device device : devices) {
                try {
                    // 获取设备类型和名称
                    String deviceType = device.getType();
                    String deviceName = device.getName();

                    View deviceView = getDeviceView(deviceType, deviceName);
                    deviceContainer.addView(deviceView);
                } catch (Exception e) {
                    // 打印错误日志，并添加一个错误提示
                    Log.e(getTag(), "add view error", e);
                    TextView errorView = new TextView(getContext());
                    errorView.setText("Error: " + e.getMessage());
                    deviceContainer.addView(errorView);
                }
            }
        } else {
            TextView noDevicesView = new TextView(getContext());
            noDevicesView.setText("No devices available");
            deviceContainer.addView(noDevicesView);
        }
    }

    private View getDeviceView(String deviceType, String deviceName) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int layoutId;

        switch (deviceType) {
            case "light":
                layoutId = R.layout.device_light;
                break;
            case "TV":
                layoutId = R.layout.device_tv;
                break;
            default:
                layoutId = R.layout.device;
                break;
        }
        View deviceView = inflater.inflate(layoutId, deviceContainer, false);

        TextView deviceNameView = deviceView.findViewById(R.id.tv_device_name);

        deviceNameView.setText(deviceName);

        return deviceView;
    }
}