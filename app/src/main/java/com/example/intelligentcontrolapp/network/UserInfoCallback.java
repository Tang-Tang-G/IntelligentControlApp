package com.example.intelligentcontrolapp.network;

public interface UserInfoCallback {
    void onSuccess(String username, String nickname);

    void onError(String error);
}
