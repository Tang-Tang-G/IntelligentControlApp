package com.example.intelligentcontrolapp.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CallbackConvert<T> implements Callback {
    CustomCallback<T> callback;
    Convert<T> converter;
    public CallbackConvert(Convert<T> converter, CustomCallback<T> callback) {
        this.callback = callback;
        this.converter = converter;
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            callback.onSuccess(converter.convert(response.body()));
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "Convert error", e);
            onFailure(call, new IOException("convert responseBody to T error"));
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        callback.onError(e.getMessage());
    }
}