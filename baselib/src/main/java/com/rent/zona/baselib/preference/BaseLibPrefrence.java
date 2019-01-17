package com.rent.zona.baselib.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseLibPrefrence {
    public static final String PREFS_FILE_NAME = "baselib";
    public static final String HTTP_TOKEN="http_token";
    public static final String HTTP_KEY="http__key";//加密用的key

    public static final String APP_VERSION_CODE= "app_version_code";

    private static SharedPreferences sPrefs = null;


    public static SharedPreferences initSharedPreferences(Context cxt) {
        if (sPrefs == null) {
            sPrefs = cxt.getSharedPreferences(PREFS_FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    public static void setHttpToken(Context context, String token){
        SharedPreferences prefs = initSharedPreferences(context);
        prefs.edit().putString(HTTP_TOKEN,token).commit();
    }
    public static String getHttpToken(Context context){
        SharedPreferences prefs = initSharedPreferences(context);
        return prefs.getString(HTTP_TOKEN, null);
    }
    public static void setHttpKey(Context context,String key){
        SharedPreferences prefs = initSharedPreferences(context);
        prefs.edit().putString(HTTP_KEY,key).commit();
    }
    public static String getHttpKey(Context context){
        SharedPreferences prefs = initSharedPreferences(context);
        return prefs.getString(HTTP_KEY, null);
    }
    public static void setAppVersionCode(Context context,int appVersionCode){
        SharedPreferences prefs = initSharedPreferences(context);
        prefs.edit().putInt(APP_VERSION_CODE,appVersionCode).commit();
    }
    public static int getAppVersionCode(Context context){
        SharedPreferences prefs = initSharedPreferences(context);
        return prefs.getInt(APP_VERSION_CODE, -1);
    }
}
