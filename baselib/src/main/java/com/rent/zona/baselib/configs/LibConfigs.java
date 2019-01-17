package com.rent.zona.baselib.configs;

import android.content.Context;


public class LibConfigs {
    public enum ServiceType{
        SERVER_ENV_TEST,//内网测试 比如192.168.1.56
        SERVER_ENV_RELEASE,//发布版本
        SERVER_ENV_INSIDE;//内网 公司内部小范围参与等
    }
    private static boolean IS_ADD_FACEBOOK_STETHO=true;//是否加入facebook的steho的调式插件
    // Global control on debug log
    public static boolean DEBUG_LOG = true;//是否打印log
    // Switch for online server
    private static ServiceType ONLINE_SERVER = ServiceType.SERVER_ENV_INSIDE;//设置服务地址的类型
    // Switch for statistical data
    private static boolean STAT_REPORT_ENABLED = false;//是否记录行为分析

    private static Context APP_CONTEXT;
//    private static Class HOME_ACTIVITY_CLASS;//主activity 暂时没用到
//    private static Class LAUNCHER_CLASS;//启动的activity 暂时没用到

    public static Context getAppContext() {
        return APP_CONTEXT;
    }

//    public static Class getHomeActivityClass() {
//        return HOME_ACTIVITY_CLASS;
//    }
//
//    public static Class getLauncherClass() {
//        return LAUNCHER_CLASS;
//    }

    public static void init(Context context,ServiceType serviceType,boolean isAddFacebookStetho,boolean isDebugLog){
        APP_CONTEXT = context;
        ONLINE_SERVER=serviceType;
        IS_ADD_FACEBOOK_STETHO=isAddFacebookStetho;
        IS_ADD_FACEBOOK_STETHO=isDebugLog;
    }
//    public static void init(Context context, Class homeActivityClass, Class launcherClass) {
//        APP_CONTEXT = context;
//        HOME_ACTIVITY_CLASS = homeActivityClass;
//        LAUNCHER_CLASS = launcherClass;
//    }

    public static void setOnlineServer(ServiceType ServerEnv) {
        ONLINE_SERVER = ServerEnv;
    }

    public static ServiceType getOnlineServer() {
        return ONLINE_SERVER;
    }

    public static boolean isDebugLog() {
        return DEBUG_LOG;
    }

    public static void setDebugLog(boolean isDebugLog) {
        DEBUG_LOG = isDebugLog;
    }

    public static boolean isStatsEnabled() {
        return STAT_REPORT_ENABLED;
    }

    public static void setStatsEnabled(boolean enable) {
        STAT_REPORT_ENABLED = enable;
    }

    public static boolean isAddFacebookStetho() {
        return IS_ADD_FACEBOOK_STETHO;
    }

    public static void setIsAddFacebookStetho(boolean isAddFacebookStetho) {
        IS_ADD_FACEBOOK_STETHO = isAddFacebookStetho;
    }
}
