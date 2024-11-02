package com.example.intelligentcontrolapp.db;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private String name;
    private List<Device> devices;

    public Area(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }
    public void removeDevice(Device device) {
        devices.remove(device);
    }
}