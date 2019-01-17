package com.rent.zona.baselib.utils;

import android.app.Activity;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.event.AppLifeCycleEvent;
import com.rent.zona.baselib.event.EventBus;
import com.rent.zona.baselib.log.LibLogger;

import java.lang.ref.WeakReference;

/**
 * Save global state variable here for later use
 */
public class GlobalStateMgr {
    private static final String TAG = "GlobalStateMgr";
    private static final int MAX_VER_COUNT = 4;

    private static WeakReference<Activity> sCurrentTopActivity;
    private static String sCurrentTopActivityName;
    private static boolean sIsForeGround;
    private static boolean sIsExited = false;



    public static Activity getCurrentTopActivity() {
        return sCurrentTopActivity != null ? sCurrentTopActivity.get() : null;
    }

    public static void setCurrentTopActivity(Activity activity) {
        if (activity != null) {
            sCurrentTopActivity = new WeakReference<Activity>(activity);
        } else {
            sCurrentTopActivity = null;
        }
    }

    public static void setCurrentTopActivityName(String activityName) {
        sCurrentTopActivityName = activityName;
    }

    public static String getCurrentTopActivityName() {
        return sCurrentTopActivityName;
    }

    public static void setIsForeGround(boolean isForeGround) {
        if (sIsForeGround != isForeGround) {
            sIsForeGround = isForeGround;
            if (isForeGround) {
                if (LibConfigs.isDebugLog()) {
                    LibLogger.d(TAG, "App Enter Foreground");
                }
                EventBus.getDefault().post(new AppLifeCycleEvent(AppLifeCycleEvent.EVENT_ENTER_FOREGROUND));
            } else {
                if (LibConfigs.isDebugLog()) {
                    LibLogger.d(TAG, "App Enter Background");
                }
                EventBus.getDefault().post(new AppLifeCycleEvent(AppLifeCycleEvent.EVENT_ENTER_BACKGROUND));
            }
        }
    }

    public static boolean isExited() {
        return sIsExited;
    }

    public static void setIsExited(boolean isExited) {
        sIsExited = isExited;
    }

    public static boolean isForeGround() {
        return sIsForeGround;
    }


}
