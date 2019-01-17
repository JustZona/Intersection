package com.rent.zona.commponent.utils;

import android.content.Context;

import com.rent.zona.baselib.preference.BaseLibPrefrence;
import com.rent.zona.baselib.utils.DeviceUtil;

/**
 * @name：
 * @author： liuyun
 * @createTime： 2017/9/13
 * @modifyTime： 2017/9/13
 * @explain：说明
 */


public class VersionCompareUtil {
    public static boolean isFirstStartUpApp(Context context){
        int lastAppCode= BaseLibPrefrence.getAppVersionCode(context);
        int currentAppCode= DeviceUtil.getVersionCode(context);
        return lastAppCode<currentAppCode;
    }

}
