package com.example.intelligentcontrolapp.network;

public interface CustomCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}
