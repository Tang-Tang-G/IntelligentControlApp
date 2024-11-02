package com.example.intelligentcontrolapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.network.ChangePasswordCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText et_new_password, et_confirm_password;
    private Button b_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        //
        et_new_password = findViewById(R.id.et_new_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        b_change_password = findViewById(R.id.b_change_password);

        //返回，销毁页面
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //修改密码点击事件
        b_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs(et_new_password.getText().toString(), et_confirm_password.getText().toString())) {
                    //获取http请求，修改密码
                    NetworkUtils.Change_Password(ChangePasswordActivity.this, et_new_password.getText().toString(), new ChangePasswordCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            // 注册失败处理
                            Toast.makeText(ChangePasswordActivity.this, "Registration failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    //输入检测
    private boolean validateInputs(String new_password, String confirm_password) {
        if (new_password.isEmpty()) {
            et_new_password.setError("Password is required");
            return false;
        }
        if (confirm_password.isEmpty()) {
            et_confirm_password.setError("confirm Password is required");
            return false;
        }
        if (!new_password.equals(confirm_password)) {
            et_confirm_password.setError("Two Password must be consistent");
            return false;
        }
        return true;
    }
}