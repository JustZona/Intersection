package com.rent.zona.baselib.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/7/3
 * @modifyTime： 2017/7/3
 * @explain：说明
 */


public class GsonFactory {
    private static Gson mGson = new GsonBuilder().create();

    public static Gson getDefault() {
        return mGson = new GsonBuilder().create();
    }
}
