package com.example.intelligentcontrolapp.network;

public interface ChangePasswordCallback {
    void onSuccess(String response);

    void onError(String error);
}
