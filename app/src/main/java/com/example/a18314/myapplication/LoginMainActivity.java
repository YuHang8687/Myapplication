package com.example.a18314.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class LoginMainActivity extends AppCompatActivity {
    private EditText name;
    private EditText pass;
    private Button sign;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);
        sign = (Button) findViewById(R.id.sign);
        login = (Button) findViewById(R.id.login);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(name.getText().toString().trim(),pass.getText().toString().trim());
                            Log.d("LoginMainActivity", "注册成功");
                        } catch (HyphenateException e) {
                            Log.d("LoginMainActivity", "注册失败"+e.getErrorCode()+e.getMessage());
                        }
                    }
                }).start();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EMClient.getInstance().login(name.getText().toString().trim(), pass.getText().toString().trim(), new EMCallBack() {
                   @Override
                   public void onSuccess() {
                       Log.d("LoginMainActivity", "登录成功");
                       Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                       intent.putExtra("username",name.getText()+"");
                       startActivity(intent);
                   }

                   @Override
                   public void onError(int i, String s) {
                       Log.d("LoginMainActivity", "登录失败,"+i+","+s);
                   }

                   @Override
                   public void onProgress(int i, String s) {

                   }
               });
            }
        });
    }
}
