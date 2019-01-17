package com.fangyizhan.intersection.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fangyizhan.intersection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAndRegisterActivity extends AppCompatActivity {

    @BindView(R.id.week_tv)
    TextView weekTv;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.register_btn)
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //默认是登录选中
        loginBtn.setBackgroundResource(R.drawable.login_bg_white);
        registerBtn.setBackground(null);
        loginBtn.setTextColor(Color.BLACK);
    }

    @OnClick({R.id.login_btn, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                //登录
                loginBtn.setBackgroundResource(R.drawable.login_bg_white);
                registerBtn.setBackground(null);
                loginBtn.setTextColor(Color.BLACK);
                break;
            case R.id.register_btn:
                //注册
                registerBtn.setBackgroundResource(R.drawable.login_bg_white);
                loginBtn.setBackground(null);
                registerBtn.setTextColor(Color.BLACK);
                break;
        }
    }
}
