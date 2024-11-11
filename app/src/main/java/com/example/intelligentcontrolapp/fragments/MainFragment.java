package com.example.intelligentcontrolapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.intelligentcontrolapp.Adapter.HomeListAdapter;
import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.db.Area;
import com.example.intelligentcontrolapp.db.Device;
import com.example.intelligentcontrolapp.db.House;
import com.example.intelligentcontrolapp.network.CustomCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;
import com.google.android.material.slider.Slider;

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
                //家庭列表设计
                labelNames.add("家庭管理"); // 添加 "Manage Homes" 选项
                homeListAdapter = new HomeListAdapter(getContext(), labelNames.toArray(new String[0]));
                //将适配器与下拉列表框关联起来
                homeList.setAdapter(homeListAdapter);

                homeListAdapter.setOnHomeSelectedListener((home, position) -> homeList.setSelection(position));

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

        return rootView;
    }

    private void showCurrentHouseDevices(List<House> houses, String home) {
        Log.d(this.getTag(), "updateDeviceList");

        List<Device> devices = houses
                .stream()
                .filter(house -> house.getName().equals(home))
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
                    int deviceID = device.getDevice_id();
                    View deviceView = getDeviceView(deviceType, deviceName,deviceID);
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

    private View getDeviceView(String deviceType, String deviceName,int deviceID) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int layoutId;
        switch (deviceType) {
            case "light":
                layoutId = R.layout.device_light;
                break;
            case "tv":
                layoutId = R.layout.device_tv;
                break;
            case "air-condition":
                layoutId = R.layout.device_air_condition;
                break;
            case "water-machine":
                layoutId = R.layout.device_water_machine;
                break;
            case "fan":
                layoutId = R.layout.device_fan;
                break;
            case "switch":
                layoutId = R.layout.device_switch;
                break;
            default:
                layoutId = R.layout.device;
                break;
        }
        View deviceView = inflater.inflate(layoutId, deviceContainer, false);

        TextView deviceNameView = deviceView.findViewById(R.id.tv_device_name);
        deviceNameView.setText(deviceName);

        TextView deviceTypeView = deviceView.findViewById(R.id.tv_device_type);
        deviceTypeView.setText(deviceType);

        SwitchCompat s = deviceView.findViewById(R.id.off_on);
        boolean currentStatus = s.isChecked();
        Log.d("MainFragment", "Current status of " + deviceName + " is " + (currentStatus ? "ON" : "OFF"));
        s.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 切换状态（开/关）
            toggleSwitch(deviceID, isChecked, deviceView);
        });

        //尝试初始化，设置滑动条的值的变化类
        Slider slider = deviceView.findViewById(R.id.slider);

        checkSlider(deviceView,slider);
        return deviceView;
    }

    private void toggleSwitch(int deviceID, boolean isChecked, View deviceView) {
        // 这里可以根据具体的实现方式，通过网络请求或本地操作更新设备状态
        // 发送网络请求更新设备状态
        String service_name = isChecked ? "open" : "close";

        NetworkUtils.getService(getContext(),deviceID,service_name, new CustomCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {

            }
            @Override
            public void onError(String message) {
                onError(message);
            }
        });
    }
    private void checkSlider(View deviceView, Slider slider) {
        if (slider != null) {

            EditText ev_value = deviceView.findViewById(R.id.value_slider);

            // 设置EditText的值变化监听器
            ev_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int value = Integer.parseInt(s.toString());
                        slider.setValue(value);
                    } catch (NumberFormatException e) {
                        // 处理无效的输入
                        Log.e("MainFragment", "Invalid input for slider value: " + s.toString(), e);
                    }
                }
            });

            slider.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    int volume = (int) value;
                    ev_value.setText(String.valueOf(volume));
                }
            });


        } else {
            Log.e("MainFragment", "Slider is null");
        }
    }


}