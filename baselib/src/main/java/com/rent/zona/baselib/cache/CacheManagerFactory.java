package com.rent.zona.baselib.cache;

import android.content.Context;
import android.text.TextUtils;

import com.rent.zona.baselib.configs.LibConfigs;


public class CacheManagerFactory {

    private static CacheManager sDefault;

    private static final String DEFAULT_CACHE_DIR = "app_default_cache";
    private static final int DEFAULT_CACHE_MEMORY_CACHE_SIZE = 4 * 1024;    // 4M
    private static final int DEFAULT_CACHE_DISK_CACHE_SIZE = 1024 * 32; // 32M

    public static CacheManager getDefault() {
        if (sDefault == null) {
            initDefaultCache(LibConfigs.getAppContext(), DEFAULT_CACHE_DIR, DEFAULT_CACHE_MEMORY_CACHE_SIZE, DEFAULT_CACHE_DISK_CACHE_SIZE);
        }
        return sDefault;
    }

    public static void initDefaultCache(Context content, String cacheDir, int memoryCacheSizeInKB, int fileCacheSizeInKB) {
        if (TextUtils.isEmpty(cacheDir)) {
            cacheDir = DEFAULT_CACHE_DIR;
        }
        if (memoryCacheSizeInKB <= 0) {
            memoryCacheSizeInKB = DEFAULT_CACHE_MEMORY_CACHE_SIZE;
        }

        if (fileCacheSizeInKB <= 0) {
            fileCacheSizeInKB = DEFAULT_CACHE_DISK_CACHE_SIZE;
        }
        sDefault = new CacheManager(content, cacheDir, memoryCacheSizeInKB, fileCacheSizeInKB);
    }
}