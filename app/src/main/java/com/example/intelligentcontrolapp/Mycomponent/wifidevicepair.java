package com.example.intelligentcontrolapp.Mycomponent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.intelligentcontrolapp.R;

public class wifidevicepair extends Dialog implements View.OnClickListener {
    EditText ssidEditText;
    EditText passwdEditText;
    EditText hostEditText;
    Button binddevice;
    Context mContext;
    Activity mActivity;

    private OnDevicePairListener onDevicePairListener;

    public wifidevicepair(@NonNull Context context, Activity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
    }

    public wifidevicepair(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected wifidevicepair(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_pair);

        ssidEditText = findViewById(R.id.ssidEditText);
        passwdEditText = findViewById(R.id.passwdEditText);
        hostEditText = findViewById(R.id.hostEditText);
        binddevice = findViewById(R.id.bindButton);

        binddevice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bindButton) {
            String ssid = ssidEditText.getText().toString();
            String passwd = passwdEditText.getText().toString();
            String host = hostEditText.getText().toString();
            if (onDevicePairListener != null) {
                onDevicePairListener.onPair(ssid, passwd, host);
            }
            dismiss();
        }
    }

    public void setOnDevicePairListener(OnDevicePairListener listener) {
        this.onDevicePairListener = listener;
    }

    public interface OnDevicePairListener {
        void onPair(String ssid, String passwd, String host);
    }
}
