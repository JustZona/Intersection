package com.fangyizhan.intersection.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.fangyizhan.intersection.utils.ShareUtil;
import com.fangyizhan.intersection.views.MyCustomDialog;
import com.fangyizhan.intersection.views.SuspensionWindow;
import com.rent.zona.baselib.cache.CacheManager;
import com.rent.zona.baselib.cache.CacheManagerFactory;
import com.rent.zona.baselib.cache.CacheMetaData;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.baselib.utils.DataCleanManager;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.set_msg_rl)
    RelativeLayout setMsgRl;
    @BindView(R.id.set_account_rl)
    RelativeLayout setAccountRl;
    @BindView(R.id.set_Blacklist_rl)
    RelativeLayout setBlacklistRl;
    @BindView(R.id.set_feedback_rl)
    RelativeLayout setFeedbackRl;
    @BindView(R.id.set_clearCache_rl)
    RelativeLayout setClearCacheRl;
    @BindView(R.id.set_outLogin_tv)
    TextView setOutLoginTv;
    @BindView(R.id.set_clear_tv)
    TextView setClearTv;
    public static void launch(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        AppTitleBar appTitleBar= (AppTitleBar) findViewById(R.id.titlebar);
        appTitleBar.setBackListener(new OnBackStackListener() {
            @Override
            public boolean onBackStack() {
                finish();
                return false;
            }
        });
        appTitleBar.setTitle("设置");
        //设置缓存的大小
        try {
            setClearTv.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.set_msg_rl, R.id.set_account_rl, R.id.set_Blacklist_rl, R.id.set_feedback_rl, R.id.set_clearCache_rl, R.id.set_outLogin_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_msg_rl://消息通知设置
                gotoNotificationSetting(this);
                break;
            case R.id.set_account_rl://账号与安全
                AccountSafeActivity.launch(this);
                break;
            case R.id.set_Blacklist_rl://黑名单

                break;
            case R.id.set_feedback_rl://意见与反馈
                break;
            case R.id.set_clearCache_rl://清除缓存
                //清除缓存
                try {
                    if (DataCleanManager.getTotalCacheSize(this).equals("0.00K")){
//                        getActivityHelper().toast("当前没有缓存",SettingActivity.this);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SuspensionWindow suspensionWindow = new SuspensionWindow(this);
                suspensionWindow.createWindow(0, 0, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                View cleanDataView = suspensionWindow.setView(R.layout.dialog_cleandata);
                suspensionWindow.PopupWindow();
                cleanDataView.findViewById(R.id.sureCleanData_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        suspensionWindow.hidePopupWindow();
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        setClearTv.setText("0.00K");
                        getActivityHelper().toast("缓存已清理",SettingActivity.this);

                    }
                });
                cleanDataView.findViewById(R.id.cancelCleanData_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        suspensionWindow.hidePopupWindow();
                    }
                });
                cleanDataView.findViewById(R.id.notCleanData_linear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        suspensionWindow.hidePopupWindow();
                    }
                });

                break;
            case R.id.set_outLogin_tv://注销登录
                //退出登录
                Map<String, ?> map = ShareUtil.GetDataAll(SettingActivity.this, "user");
                if (map == null || map.size() == 0) {
                    getActivityHelper().toast("亲，你还未登录", SettingActivity.this);
                    finish();
                    return;
                }
                String isLogin= (String) map.get("loginStatus");
                if (!isLogin.equals("true")){
                    return;
                }
                //退出登录
                MyCustomDialog dialog1 = new MyCustomDialog(SettingActivity.this);
                dialog1.show();
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.setTitleText("退出登录");
                dialog1.setINfoText("确认退出登录？");
                dialog1.setLeftButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.setRightButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*TIMManager.getInstance().logout(new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                                //错误码 code 列表请参见错误码表
                                Log.d("LOG", "logout failed. code: " + code + " errmsg: " + desc);
                            }
                            @Override
                            public void onSuccess() {
                                //登出成功 直接跳转到登录界面 这里用不上
                               *//* Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                                intent.putExtra("closeType",1);
                                startActivity(intent);
                                ShareUtil.DeleteDataAll(SettingActivity.this, "loginStatus");//删除user信息
                                SaveCommand.setUserBean(null);*//*
                                //退出登录
                                //退出登录清除推送消息 防止一个设备登录了多个账号 接收到别人账号的推送

                                sendRequest(CommonServiceFactory.getInstance().commonService().userOutClearData(Config.user_id+""), r -> {
                                    String msg = (String)r.msg;
                                    getActivityHelper().toast(msg,SettingActivity.this);
                                    ShareUtil.DeleteDataAll(SettingActivity.this, "user");//删除user信息
                                    ShareUtil.DeleteDataAll(SettingActivity.this, "token");//清除token信息
                                    SaveCommand.setUserBean(null);
                                    //推送反注册
//                                    XGPushManager.unregisterPush(SettingActivity.this);
                                    Config.loginStatus="false";
                                    Config.user_id=0;
                                    BadgeUtil.removeCount(SettingActivity.this);//清除了推送消息 角标全部移除
                                    Config.badge=0;
                                    finish();
                                },e->{
                                    if (e.getMessage().contains("TaskException")){
                                        getActivityHelper().toast(((TaskException)e).getDesc(), SettingActivity.this);
                                    }else {
                                        getActivityHelper().toast(e.getMessage(), SettingActivity.this);
                                    }
                                });

                            }
                        });*/
                    }
                });
                break;
        }
    }
    public static void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, 1001);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" +activity.getPackageName()));
                activity.startActivityForResult(intent, 1001);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, 1001);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, 1001);
//            MyLog.e(sTAG, e);
        }
    }
}
