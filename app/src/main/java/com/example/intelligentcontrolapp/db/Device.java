package com.example.intelligentcontrolapp.db;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private int deviceID;
    private String name;
    private  String type;
    public Device(int device_id,String name, String type) {
        this.deviceID=device_id;
        this.name = name;
        this.type =type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDevice_id()
    {
        return deviceID;
    }
}