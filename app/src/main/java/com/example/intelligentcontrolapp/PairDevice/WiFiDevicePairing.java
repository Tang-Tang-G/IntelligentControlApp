package com.example.intelligentcontrolapp.PairDevice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class WiFiDevicePairing extends Pair {
    private static final String DEFAULT_DEVICE_IP = "192.168.4.1";
    private static final int DEFAULT_PORT = 45678;


    private final String gateway;
    private final int port;  // Define your port
    private final Message message;

    public WiFiDevicePairing(Message message) {
        this(DEFAULT_DEVICE_IP, DEFAULT_PORT, message);
    }

    public WiFiDevicePairing(String gateway, int port, Message message) {
        this.gateway = gateway;
        this.port = port;
        this.message = message;
    }

    @Override
    void onMessageOk() {
        Log.d("pair", "message ok");

    }

    @Override
    void onAskConfig() {
        Log.d("pair", "config");

    }

    @Override
    void onMessageErr() {
        Log.d("pair", "message err");

    }

    @Override
    void onWiFiConnected() {
        Log.d("pair", "wifi ok");

    }

    @Override
    void onHostConnected() {
        Log.d("pair", "host ok");
        
    }

    @Override
    void onOtherError() {
        Log.d("pair", "other error");
    }

    @Override
    public boolean pair() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(gateway, port), 500);
            if (devicePair(socket.getOutputStream(), socket.getInputStream(), message)) {
                return true;
            }
        } catch (IOException e) {
            e.fillInStackTrace();
            Log.e("pair", Objects.requireNonNull(e.getMessage()));
        } catch (PairingError e) {
            Log.e("pair", e.message + " " + e.status);
            return false;
        }
        return false;
    }

    public class WiFiDevicePairingTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return pair();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d("WiFiDevicePairingTask", "Device paired successfully");
            } else {
                Log.e("WiFiDevicePairingTask", "Failed to pair device");
            }
        }
    }
}