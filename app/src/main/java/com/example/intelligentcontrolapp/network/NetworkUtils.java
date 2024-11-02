package com.example.intelligentcontrolapp.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    //登录申请，检查了用户名和密码
    public static void login(Context context, String username, String password, LoginCallback callback) {
        new Thread(() -> {
            try {
                String UrlString = context.getString(R.string.URL_LOGIN);
                HttpURLConnection connection = getHttpURLConnection(UrlString, true);
                //连接服务器
                connection.connect();
                // 发送登录数据
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                String loginJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

                writer.write(loginJson);
                writer.flush();
                writer.close();
                // 处理响应
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 读取响应数据
                    InputStream inputStream = connection.getInputStream();
                    String responseString = getStringFromInputStream(inputStream);

                    JSONObject jsonResponse = new JSONObject(responseString);
                    int code = jsonResponse.getInt("code");

                    if (code == 200) {
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String token = data.getString("token");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(token));
                    } else {
                        String message = jsonResponse.getString("message");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onError(message));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            callback.onError("Login failed: " + connection.getResponseMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Exception occurred: ", e);
                new Handler(Looper.getMainLooper()).post(() -> callback.onError("Error during login: " + e.getMessage()));
            }
        }).start();
    }

    //注册申请，上传数据
    public static void register(Context context, String username, String password, RegisterCallback callback) {
        new Thread(() -> {
            try {
                Log.d("NetworkUtils", "Starting register request");
                String urlString = context.getString(R.string.URL_REGISTER); // 获取字符串资源
                HttpURLConnection connection = getHttpURLConnection(urlString, true);

                connection.connect();
                // 发送注册数据
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                String registerJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
                writer.write(registerJson);
                writer.flush();
                writer.close();

                // 处理响应
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 读取响应数据
                    InputStream inputStream = connection.getInputStream();
                    String responseString = getStringFromInputStream(inputStream);
                    String result = "Success: " + responseCode + " --- " + connection.getResponseMessage() + " --- " + responseString;
                    Log.d("NetworkUtils", result);
                    callback.onSuccess(responseString);  // 使用responseString
                } else {
                    String result = "Error: " + responseCode + " --- " + connection.getResponseMessage();
                    Log.d("NetworkUtils", result);
                    callback.onError(result);
                }

            } catch (Exception e) {
                Log.e("NetworkUtils", "Exception occurred: ", e);
                callback.onError(e.toString());
            }
        }).start();
    }

    //修改密码
    public static void Change_Password(Context context, String new_password, ChangePasswordCallback callback) {
        new Thread(() -> {
            try {
                Log.d("NetworkUtils", "Starting change_password request");
                String UrlString = context.getString(R.string.URL_LOGIN);
                HttpURLConnection connection = getHttpURLConnection(UrlString, false);

                String token = MyApplication.getPreferencesManager().getToken();
                connection.setRequestProperty("Authorization", "Bearer " + token);

                connection.connect();

                // 发送修改的数据
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                String registerJson = String.format("{\"password\":\"%s\"}", new_password);
                writer.write(registerJson);
                writer.flush();
                writer.close();

                // 处理响应
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 读取响应数据
                    InputStream inputStream = connection.getInputStream();
                    String responseString = getStringFromInputStream(inputStream);
                    String result = "Success: " + responseCode + " --- " + connection.getResponseMessage() + " --- " + responseString;
                    Log.d("NetworkUtils", result);
                    callback.onSuccess(responseString);  // 使用responseString
                } else {
                    String result = "Error: " + responseCode + " --- " + connection.getResponseMessage();
                    Log.d("NetworkUtils", result);
                    callback.onError(result);
                }

            } catch (Exception e) {
                Log.e("NetworkUtils", "Exception occurred: ", e);
                callback.onError(e.toString());
            }
        }).start();
    }

    //获取用户信息
    public static void getUserInfo(Context context, UserInfoCallback callback) {
        new Thread(() -> {
            try {
                String UrlString = context.getString(R.string.URL_LOGIN);
                HttpURLConnection connection = getHttpURLConnection(UrlString, false);

                String token = MyApplication.getPreferencesManager().getToken();

                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String responseString = getStringFromInputStream(inputStream);

                    JSONObject jsonResponse = new JSONObject(responseString);
                    int code = jsonResponse.getInt("code");

                    if (code == 200) {
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String username = data.getString("username");
                        String nickname = data.getString("nickname");

                        //假设收到了nickname
                        nickname = "nickname";
                        String finalNickname = nickname;
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(username, finalNickname));
                    } else {
                        String message = jsonResponse.getString("message");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onError(message));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            callback.onError("Fetch user info failed: " + connection.getResponseMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError("Error during fetch user info: " + e.getMessage()));
            }
        }).start();
    }

    //获取所有的信息
    public static void getDataInfo(Context context, DataCallback callback) {
        new Thread(() -> {
            try {
//                String urlString = context.getString(R.string.MESSAGE); // 信息的URL
                HttpURLConnection connection = getHttpURLConnection("http://47.108.27.238/api/my/device", false);
                //发送请求头
//                String token = MyApplication.getPreferencesManager().getToken();
                String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Mywic3ViIjoid3p5Iiwicm9sZSI6InVzZXIiLCJleHAiOjE3MzA1NjE5NDJ9.UsHPockSZ_0RYJaAeDq2qHGc4FveIFYDR7Vbd68oj2I";
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();
                int responseCode = connection.getResponseCode();

                Log.e("DataInfo","Responsecode:"+responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String responseString = getStringFromInputStream(inputStream);
                    JSONObject jsonResponse = new JSONObject(responseString);
                    int code = jsonResponse.getInt("code");

                    Log.e("DataInfo","code:"+code);

                    if (code == 200) {
                        // 假设返回的信息在data字段中
                        JSONObject DataInfo = jsonResponse.getJSONObject("data");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(DataInfo));
                    }
                    else
                    {
                        String message = jsonResponse.getString("message");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onError(message));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            callback.onError("Fetch device info failed: " + connection.getResponseMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError("Error during fetch device info: " + e.getMessage()));
            }
        }).start();
    }

    //获取HttpURLConnection
    public static @NonNull HttpURLConnection getHttpURLConnection(String UrlString, boolean IsPost) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(UrlString).openConnection();
        // 配置连接参数
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
        if (IsPost) {
            connection.setRequestMethod("POST");
        } else {
            connection.setRequestMethod("GET");
        }

        connection.setConnectTimeout(60 * 1000);
        connection.setReadTimeout(60 * 1000);

        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }

    //将input_stream转换为String
    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            bao.write(buffer, 0, len);
        }
        is.close();
        return bao.toString("UTF-8");
    }
}
