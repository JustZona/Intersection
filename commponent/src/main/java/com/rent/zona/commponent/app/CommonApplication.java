package com.rent.zona.commponent.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.Config;

import com.facebook.stetho.Stetho;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.pullrefresh.header.DefaultClassicsHeader;
import com.rent.zona.commponent.utils.ProcessUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareConfig;

//import cn.jpush.android.api.JPushInterface;

/**
 * 项目中所有Application 都需继承此Application
 */
public class CommonApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LibConfigs.init(this, LibConfigs.getOnlineServer(), true, true);
        if (LibConfigs.isAddFacebookStetho()) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
        initRefreshLayout();//初始化刷新控件的风格
//        JPushInterface.setDebugMode(LibConfigs.isDebugLog());
//        JPushInterface.init(this);
        initUment();//友盟初始化
        initBugyly();//初始化bugly crash

    }

    private void initBugyly() {
        // 获取当前包名
       /* String packageName = getPackageName();
        // 获取当前进程名
        String processName = ProcessUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(this, getApplicationMetaData("BUGLY_APP_ID"), LibConfigs.isDebugLog(), strategy);*/
    }

    private void initUment() {
        UMConfigure.setLogEnabled(LibConfigs.isDebugLog());

       /* UMConfigure.init(this, getApplicationMetaData("UMENG_APPKEY")
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
//        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5992ded77666133b68000b6b");
        PlatformConfig.setWeixin(getApplicationMetaData("WINXIN_ID"), getApplicationMetaData("WINXIN_KEY"));
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo(getApplicationMetaData("SINA_WEIBO_KEY"), getApplicationMetaData("SINA_WEIBO_SECRET"), getApplicationMetaData("SINA_WEIBO_REDIRECTURL"));//http://sns.whalecloud.com
        PlatformConfig.setQQZone(getApplicationMetaData("qqappid"), getApplicationMetaData("QQ_KEY"));*/
    }

    private String getApplicationMetaData(String metaName) {
        try {
            System.out.println("getPackageName-******************>" + getPackageName());
            Object metaData = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.get(metaName);
            return metaData.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LibLogger.de("meta_data获取异常", "没有name为" + metaName + "的meta-data");
            return "";
        }
    }

    /**
     * 统一化 refreshlayout的加载Header footer
     *
     * @author liuyun
     * @date 2018/4/23 0023
     */
    private void initRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, R.color.common_dark);//全局设置主题颜色
                return new DefaultClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

}
