package com.fangyizhan.intersection.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fangyizhan.intersection.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdatePwdActivity extends BaseActivity {

    @BindView(R.id.oldPwd_et)
    EditText oldPwdEt;
    @BindView(R.id.newPwd_et)
    EditText newPwdEt;
    @BindView(R.id.update_psd_rl)
    LinearLayout updatePsdRl;
    @BindView(R.id.confirmPwd_et)
    EditText confirmPwdEt;
    public static void launch(Context context) {
        context.startActivity(new Intent(context, UpdatePwdActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        AppTitleBar appTitleBar= (AppTitleBar) findViewById(R.id.titlebar);
        appTitleBar.setBackListener(new OnBackStackListener() {
            @Override
            public boolean onBackStack() {
                finish();
                return false;
            }
        });
        appTitleBar.setTitle("修改密码");
        appTitleBar.setRightBar("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
            }
        });
    }
}
