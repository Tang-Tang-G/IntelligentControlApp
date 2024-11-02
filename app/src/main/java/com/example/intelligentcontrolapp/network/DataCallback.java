package com.example.intelligentcontrolapp.network;

import org.json.JSONObject;

public interface DataCallback {
    void onSuccess(JSONObject DataInfo);
    void onError(String errorMessage);
}
