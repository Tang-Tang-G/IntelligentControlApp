package com.example.intelligentcontrolapp.network;

public interface RegisterCallback {
    void onSuccess(String response);

    void onError(String error);
}
