package com.example.intelligentcontrolapp.network;

public interface LoginCallback {
    void onSuccess(String token);

    void onError(String error);
}
