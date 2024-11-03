package com.example.intelligentcontrolapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.network.CustomCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username, et_password, et_confirm_password;
    private Button b_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化控件
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        b_register = findViewById(R.id.b_register);
        //返回
        findViewById(R.id.toolbar).setOnClickListener(view -> finish());

        // 监听用户名输入框的失去焦点事件
        et_username.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String username = et_username.getText().toString();
            }
        });

        //监听 密码强度检验 失去焦点事件
        et_password.setOnFocusChangeListener((view, b) -> {
            if (!isPasswordStrong(et_password.getText().toString())) {
                et_password.setError("Password must be at least 8 characters long, containing uppercase, lowercase, number and special character.");
            }
        });

        //点击注册按钮
        b_register.setOnClickListener(view -> {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            String confirmPassword = et_confirm_password.getText().toString();
            if (validateInputs(username, password, confirmPassword)) {
                NetworkUtils.register(RegisterActivity.this, username, password, new CustomCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //输入检测
    private boolean validateInputs(String username, String password, String confirmPassword) {
        if (username.isEmpty()) {
            et_username.setError("Username is required");
            return false;
        }
        if (password.isEmpty()) {
            et_password.setError("Password is required");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            et_confirm_password.setError("confirm Password is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            et_confirm_password.setError("Two Password must be consistent");
            return false;
        }
        return true;
    }

    //密码强度校验
    private boolean isPasswordStrong(String password) {
        // 最少8位，包含大写字母、小写字母、数字和符号
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }
}
