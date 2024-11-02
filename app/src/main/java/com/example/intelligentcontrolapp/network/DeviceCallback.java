package com.example.intelligentcontrolapp.network;

public interface DeviceCallback {
    void onSuccess(String deviceInfo);
    void onError(String errorMessage);
}
