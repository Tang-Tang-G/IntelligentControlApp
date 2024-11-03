package com.example.intelligentcontrolapp.network;

import okhttp3.ResponseBody;

public interface Convert<T> {
    T convert(ResponseBody body) throws Exception;
}
