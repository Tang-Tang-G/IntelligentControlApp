package com.example.intelligentcontrolapp.activities;

import static com.example.intelligentcontrolapp.MyApplication.isPasswordStrong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intelligentcontrolapp.MyApplication;
import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.fragments.MyFragment;
import com.example.intelligentcontrolapp.network.LoginCallback;
import com.example.intelligentcontrolapp.network.NetworkUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_login_username);
        et_password = findViewById(R.id.et_login_password);

        //返回，销毁页面
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //点击注册
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //忘记密码
        findViewById(R.id.tv_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        //登录
        findViewById(R.id.b_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if (password.isEmpty() || username.isEmpty() || !isPasswordStrong(password)) {
                    Toast.makeText(LoginActivity.this, "请输入有效的用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                NetworkUtils.login(LoginActivity.this, username, password, new LoginCallback() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d("LoginActivity", "Login success: token:" + token);
                        // 登录成功后的逻辑
                        // 例如保存 token、username  并跳转到主界面
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        MyApplication.getPreferencesManager().saveToken(token);
                        MyApplication.getPreferencesManager().saveUserInfo(username);
                        // 登录成功后，启动 MainActivity 并传递需要显示的片段信息
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("LoginActivity", "Login error: " + error);
                        // 处理登录失败的逻辑
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}