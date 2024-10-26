package com.example.intelligentcontrolapp.network;

public interface LoginCallback {
    void onSuccess(String response);

    void onError(String error);
}
