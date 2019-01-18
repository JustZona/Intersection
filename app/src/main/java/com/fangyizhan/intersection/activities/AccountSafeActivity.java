package com.fangyizhan.intersection.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fangyizhan.intersection.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSafeActivity extends BaseActivity {

    @BindView(R.id.change_phone_rl)
    RelativeLayout changePhoneRl;
    @BindView(R.id.update_psd_rl)
    RelativeLayout updatePsdRl;
    public static void launch(Context context) {
        context.startActivity(new Intent(context, AccountSafeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        AppTitleBar appTitleBar= (AppTitleBar) findViewById(R.id.titlebar);
        appTitleBar.setBackListener(new OnBackStackListener() {
            @Override
            public boolean onBackStack() {
                finish();
                return false;
            }
        });
        appTitleBar.setTitle("账号与安全");
    }

    @OnClick({R.id.change_phone_rl, R.id.update_psd_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_phone_rl://更换手机号
                ChangePhoneActivity.launch(this);
                break;
            case R.id.update_psd_rl://修改密码
                UpdatePwdActivity.launch(this);
                break;
        }
    }
}
