package com.rent.zona.baselib.configs;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FilePathConfig {
    private static final String CACHE_IMAGE="images";

    /**
     * 获取图片缓存路径
     * @param context
     * @return
     */
    public static String getImageCacheDir(Context context){
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath()+ File.separator+CACHE_IMAGE+ File.separator;
        } else {
            cachePath = context.getCacheDir().getPath()+ File.separator+CACHE_IMAGE+ File.separator;
        }
        if(!new File(cachePath).exists()){
            new File(cachePath).mkdirs();
        }
        return cachePath;
    }
}
