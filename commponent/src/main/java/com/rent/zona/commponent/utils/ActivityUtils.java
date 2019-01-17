package com.rent.zona.commponent.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.rent.zona.commponent.views.AppTitleBar;

import java.util.List;


public class ActivityUtils {
    public static void hideKeyBoard(Activity activity) {
        hideKeyBoard(activity, false);
    }

    public static void hideKeyBoard(Activity activity, boolean clearFocus) {
        View view = activity.getCurrentFocus();
        hideKeyBoard(activity, view, clearFocus);
    }

    public static void hideKeyBoard(Activity activity, View view, boolean clearFocus) {
        if (view != null) {
            if (clearFocus) {
                view.clearFocus();
            }
            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取键盘是否已经打开
     */
    public static boolean isKeyboardShowing(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isAcceptingText();
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.showSoftInput(view, 0);
//        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 检测当前App是否在前台运行
     *
     * @param context
     * @return true 前台运行，false 后台运行
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        // 正在运行的应用
        ActivityManager.RunningTaskInfo foregroundTask = runningTasks.get(0);
        String packageName = foregroundTask.topActivity.getPackageName();
        String myPackageName = context.getPackageName();

        // 比较包名
        return packageName.equals(myPackageName);
    }


    public static ComponentName getTopActivity(ActivityManager am) {
        List<ActivityManager.RunningTaskInfo> taskList = null;
        try {
            taskList = am.getRunningTasks(1);
            if (taskList != null && taskList.size() > 0) {
                ComponentName cname = taskList.get(0).topActivity;
                return cname;
            }
        } catch (Exception e) {
            // should not be here, but in some system, the getRunningTasks will fail with crash...
        }
        return null;
    }

    public static boolean isTopActivity(String className, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentName = getTopActivity(am);
        if (componentName != null) {
            return componentName.getClassName().contains(className);
        }

        return false;
    }

    public static AppTitleBar findTitleBar(Activity activity, int titlebarId) {
        return (AppTitleBar) activity.getWindow().getDecorView().findViewById(titlebarId);
    }
    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
