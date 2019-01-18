package com.fangyizhan.intersection.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.scwang.smartrefresh.layout.util.DesignUtil;

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
    @BindView(R.id.login_register_bt)
    Button loginRegisterBt;

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
        //获取手机屏幕高度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//单位是px
        int height = wm.getDefaultDisplay().getHeight();
        //设置白色倒月牙距离上方的位置 weight42 +交叉的高度
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) loginRegisterBt.getLayoutParams();
        params.setMargins(0, (int) (height/3*2- DensityUtil.dp2px(25)), 0, 0);
        loginRegisterBt.setLayoutParams(params);
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
