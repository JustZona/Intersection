package com.fangyizhan.intersection.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAndRegisterActivity extends BaseActivity {

    @BindView(R.id.week_tv)
    TextView weekTv;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.login_register_bt)
    Button loginRegisterBt;
    @BindView(R.id.register_number_et)
    EditText registerNumberEt;
    @BindView(R.id.register_yzm_et)
    EditText registerYzmEt;
    @BindView(R.id.register_newPwd_et)
    EditText registerNewPwdEt;
    @BindView(R.id.register_linear)
    LinearLayout registerLinear;
    @BindView(R.id.login_register_linear)
    LinearLayout login_register_linear;
    @BindView(R.id.login_number_et)
    EditText loginNumberEt;
    @BindView(R.id.login_yzm_et)
    EditText loginYzmEt;
    @BindView(R.id.login_linear)
    LinearLayout loginLinear;
    @BindView(R.id.forgetPwd_tv)
    TextView forgetPwdTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //默认是登录选中
        initLogin();
    }

    private void initRegister() {
        registerBtn.setBackgroundResource(R.drawable.login_bg_white);
        login_register_linear.setBackgroundResource(R.drawable.login_register_bg);
        login_register_linear.getBackground().setAlpha(38);//设计图是15% （这里是0-255）转换过来的
        loginBtn.setBackground(null);
        loginBtn.setTextColor(Color.WHITE);
        registerBtn.setTextColor(Color.BLACK);
        registerLinear.setVisibility(View.VISIBLE);
        loginLinear.setVisibility(View.GONE);
        forgetPwdTv.setVisibility(View.GONE);
        //获取手机屏幕高度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//单位是px
        int height = wm.getDefaultDisplay().getHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) loginRegisterBt.getLayoutParams();
        params.setMargins(0, (int) (height / 2.9 * 1.9 - DensityUtil.dp2px(25)), 0, 0);
        loginRegisterBt.setLayoutParams(params);
    }

    private void initLogin() {
        loginBtn.setBackgroundResource(R.drawable.login_bg_white);
        login_register_linear.setBackgroundResource(R.drawable.login_register_bg);
        login_register_linear.getBackground().setAlpha(38);//设计图是15% （这里是0-255）转换过来的
        registerBtn.setBackground(null);
        registerBtn.setTextColor(Color.WHITE);
        loginBtn.setTextColor(Color.BLACK);
        registerLinear.setVisibility(View.GONE);
        loginLinear.setVisibility(View.VISIBLE);
        forgetPwdTv.setVisibility(View.VISIBLE);
        //获取手机屏幕高度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//单位是px
        int height = wm.getDefaultDisplay().getHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) loginRegisterBt.getLayoutParams();
        params.setMargins(0, (int) (height / 2.5 * 1.5 - DensityUtil.dp2px(25)), 0, 0);
        loginRegisterBt.setLayoutParams(params);
    }

    @OnClick({R.id.login_btn, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                //登录
                initLogin();
                break;
            case R.id.register_btn:
                //注册
                initRegister();
                break;
        }
    }
}
