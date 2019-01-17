package com.rent.zona.commponent.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by zy on 2017/2/8.
 */

public class MyActivityManager {
    private static Stack<Activity> activityStack;
    private static MyActivityManager activityManager;
    private MyActivityManager(){}

    public static MyActivityManager getInstance(){
        if (activityManager == null) {
            synchronized (MyActivityManager.class){
                if (activityManager==null){
                    activityManager = new MyActivityManager();
                }
            }
        }
        return activityManager;
    }

    public void addActivity(Activity activity){
        if (activityStack==null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity(){
        return activityStack.lastElement();
    }

    public Activity getActivity(Class clazz){
        for (int i = 0;i<activityStack.size();i++){
            if (activityStack.get(i).getClass().getName().equals(clazz.getName())){
                return activityStack.get(i);
            }
        }
        return null;
    }

    public void finishActivity(){
        finishActivity(activityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
