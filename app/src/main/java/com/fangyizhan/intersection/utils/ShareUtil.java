package com.fangyizhan.intersection.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.util.SimpleArrayMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 2017/4/8.
 * Share工具类.
 */
public class ShareUtil {

    /**
     * 保存数据.
     * @param context
     * @param name
     * @param map
     */
    public static void SaveData(Context context, String name, SimpleArrayMap<String,?> map){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for(int i = 0;i<map.size();i++){
            if (map.valueAt(i) instanceof String){
                editor.putString(map.keyAt(i), String.valueOf(map.valueAt(i)));
            }
            if (map.valueAt(i) instanceof Boolean){
                editor.putBoolean(map.keyAt(i), (Boolean) map.valueAt(i));
            }
            if (map.valueAt(i) instanceof Float){
                editor.putFloat(map.keyAt(i), (Float) map.valueAt(i));
            }
            if (map.valueAt(i) instanceof Long){
                editor.putLong(map.keyAt(i), (Long) map.valueAt(i));
            }
            if (map.valueAt(i) instanceof Set){
                editor.putStringSet(map.keyAt(i), (Set) map.valueAt(i));
            }

        }
        editor.commit();
    }

    /**
     * 批量删除数据.
     * @param context
     * @param name
     */
    public static void DeleteDataAll(Context context, String name){
        Map<String,?> map = GetDataAll(context,name);
        for(String m: map.keySet()){
            DeleteData(context,name,m);
        }
    }

    /**
     * 删除数据.
     * @param context
     * @param name
     */
    public static void DeleteData(Context context, String name, String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static String GetData(Context context, String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static int GetDataInt(Context context, String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getInt(key,-1);
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static boolean GetDataBoolean(Context context, String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static Float GetDataFloat(Context context, String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getFloat(key,-1);
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static Long GetDataLong(Context context, String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getLong(key,-1);
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @return
     */
    public static Map<String, ?> GetDataAll(Context context, String name){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getAll();
    }

    /**
     * 获取数据.
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static Set<String> GetDataStringSet(Context context, String name, String key){
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getStringSet(key,null);
    }

    /**
     * 获取其他app的ShareContext对象.
     * @param context
     * @param Package
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static Context GetOtherAppShareContext(Context context,String Package){
        Context c = null;
        try {
            c = context.createPackageContext(Package, Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
}
