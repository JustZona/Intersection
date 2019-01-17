package com.rent.zona.commponent.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.dlg.CommonDialog;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

public class TestShareActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_share);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(TestShareActivity.this)
                        .permission(
                                Permission.WRITE_EXTERNAL_STORAGE,
                                Permission.ACCESS_FINE_LOCATION,
//                                Permission.CALL_PHONE,
//                                Permission.READ_LOGS,
                                Permission.READ_PHONE_STATE,
                                Permission.READ_EXTERNAL_STORAGE
//                                Permission.SET_DEBUG_APP,
//                                Permission.SYSTEM_ALERT_WINDOW,
//                                Permission.GET_ACCOUNTS
//                                Permission.WRITE_APN_SETTINGS
                        )
                        .onGranted(new Action() {

                            @Override
                            public void onAction(List<String> permissions) {
                                LibLogger.di("权限","同意");
//                                startLoc();
                                share();
                            }
                        }).onDenied(new Action(){
                    @Override
                    public void onAction(List<String> permissions) {
                        LibLogger.di("权限","拒绝");
                        if (AndPermission.hasAlwaysDeniedPermission(TestShareActivity.this, permissions)) {
                            // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                            CommonDialog dialog=new CommonDialog(TestShareActivity.this);
                            dialog.setTitle("温馨提示");
                            List<String> permissionNames = Permission.transformText(TestShareActivity.this, permissions);
                            dialog.setMessage("没有权限 "+TextUtils.join(",\n", permissionNames)+"\n 无法继续运行");
                            dialog.setOkBtn("去设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 如果用户继续：
                                    AndPermission.permissionSetting(TestShareActivity.this).execute();
                                }
                            });
//                        dialog.setCancelBtn("算了", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 如果用户中断：
//                                executor.cancel();
//                            }
//                        });
                            dialog.show();
                        }
                    }
                }).rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        LibLogger.di("权限","showRationale");
// 这里使用一个Dialog询问用户是否继续授权。
                        List<String> permissionNames = Permission.transformText(context, permissions);
                        CommonDialog dialog=new CommonDialog(TestShareActivity.this);
                        dialog.setTitle("温馨提示");
                        dialog.setMessage("需要权限 "+ TextUtils.join(",\n", permissionNames));
                        dialog.setOkBtn("继续", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果用户继续：
                                executor.execute();
                            }
                        });

                        dialog.setCancelBtn("算了", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果用户中断：
                                executor.cancel();
                            }
                        });
                        dialog.show();
                    }
                }).start();
            }
        });
    }
    private void share(){
         UMShareListener shareListener = new UMShareListener() {
            /**
             * @descrption 分享开始的回调
             * @param platform 平台类型
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {
                showProgress("");
                System.out.println("分享开始");
            }

            /**
             * @descrption 分享成功的回调
             * @param platform 平台类型
             */
            @Override
            public void onResult(SHARE_MEDIA platform) {
                dismissProgress();
                ActivityUIHelper.toast("分享成功",TestShareActivity.this);
                System.out.println("分享成功");
            }

            /**
             * @descrption 分享失败的回调
             * @param platform 平台类型
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {

                dismissProgress();
                LibLogger.de("分享失败",t.getMessage());
                t.printStackTrace();
                ActivityUIHelper.toast("分享失败",TestShareActivity.this);
                System.out.println("分享失败");
            }

            /**
             * @descrption 分享取消的回调
             * @param platform 平台类型
             */
            @Override
            public void onCancel(SHARE_MEDIA platform) {
                dismissProgress();
                ActivityUIHelper.toast("取消分享",TestShareActivity.this);
                System.out.println("取消分享");

            }
        };
        UMImage thumb = new UMImage(this, "https://www.baidu.com/img/bd_logo1.png?where=super");//图片地址
        UMWeb web = new UMWeb("https://www.baidu.com/?tn=baiduhome_pg");//链接
        web.setThumb(thumb);
        web.setDescription("我叫百度，有什么问题可以随时问我哦");//内容
        web.setTitle("你好，百度");//标题
        new ShareAction(TestShareActivity.this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
        //QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
