package com.fangyizhan.intersection.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.fangyizhan.intersection.views.WelcomeSkipButton;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.edit_phone_et)
    EditText editPhoneEt;
    @BindView(R.id.delete1_iv)
    ImageView delete1Iv;
    @BindView(R.id.onNext_bt)
    Button onNextBt;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.info_tv)
    TextView infoTv;
    @BindView(R.id.info2_tv)
    TextView info2Tv;
    @BindView(R.id.edit_code_et)
    EditText editCodeEt;
    @BindView(R.id.code_delete1_iv)
    ImageView codeDelete1Iv;
    @BindView(R.id.yzm_bt)
    WelcomeSkipButton yzmBt;
    @BindView(R.id.sure_bt)
    Button sureBt;
    @BindView(R.id.linear2)
    LinearLayout linear2;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ChangePhoneActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        AppTitleBar appTitleBar= (AppTitleBar) findViewById(R.id.titlebar);
        appTitleBar.setBackListener(new OnBackStackListener() {
            @Override
            public boolean onBackStack() {
                finish();
                return false;
            }
        });
        appTitleBar.setTitle("更换手机号");
    }

    @OnClick({R.id.delete1_iv, R.id.onNext_bt,R.id.code_delete1_iv, R.id.yzm_bt, R.id.sure_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delete1_iv://删除
                if (TextUtils.isEmpty(editPhoneEt.getText().toString().trim())) {
                    return;
                }
                editPhoneEt.setText("");
                break;
            case R.id.onNext_bt://下一步
                //这里需要校验手机号
                if (TextUtils.isEmpty(editPhoneEt.getText().toString().trim())||editPhoneEt.getText().toString().trim().equals("请输入手机号")){
                    getActivityHelper().toast("请输入正确的手机号码",this);
                    return;
                }
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                break;
            case R.id.code_delete1_iv://删除输入的验证码
                if (TextUtils.isEmpty(editCodeEt.getText().toString().trim())) {
                    return;
                }
                editCodeEt.setText("");
                break;
            case R.id.yzm_bt://获取验证码

                break;
            case R.id.sure_bt://确定
                //请求后台接口 成功之后 finish
                break;
        }
    }

}
