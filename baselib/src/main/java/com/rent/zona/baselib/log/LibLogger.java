package com.rent.zona.baselib.log;

import android.util.Log;

import com.rent.zona.baselib.configs.LibConfigs;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
* 日志打印类  前缀“d”的方法 表示只有当日志开关打开的时候 才能打印
* @author liuyun
* @date 2018/4/17 0017
*/
public class LibLogger {
    public static final String TAG = "LibLogger";

    public static void v(String subTag, String msg) {
        Log.v(TAG, getLogMsg(subTag, msg));
    }

    public static void d(String subTag, String msg) {
        Log.d(TAG, getLogMsg(subTag, msg));
    }

    public static void i(String subTag, String msg) {
        Log.i(TAG, getLogMsg(subTag, msg));
    }

    public static void w(String subTag, String msg) {
        Log.w(TAG, getLogMsg(subTag, msg));
    }

    public static void w(String subTag, String msg, Throwable e) {
        Log.w(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
    }

    public static void e(String subTag, String msg) {
        Log.e(TAG, getLogMsg(subTag, msg));
    }

    public static void e(String subTag, String msg, Throwable e) {
        Log.e(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
    }

    private static String getLogMsg(String subTag, String msg) {
        return "[" + subTag + "] " + msg;
    }

    private static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter(1024);
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }



    public static void dv(String subTag, String msg) {
        if( LibConfigs.isDebugLog()){
            Log.v(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void dd(String subTag, String msg) {
        if( LibConfigs.isDebugLog()){
            Log.d(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void di(String subTag, String msg) {
        if( LibConfigs.isDebugLog()){
            Log.i(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void dw(String subTag, String msg) {
        if( LibConfigs.isDebugLog()){
            Log.w(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void dw(String subTag, String msg, Throwable e) {
        if( LibConfigs.isDebugLog()){
            Log.w(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
        }
    }

    public static void de(String subTag, String msg) {
        if( LibConfigs.isDebugLog()){
            Log.e(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void de(String subTag, String msg, Throwable e) {
        if( LibConfigs.isDebugLog()){
            Log.e(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
        }
    }
}
