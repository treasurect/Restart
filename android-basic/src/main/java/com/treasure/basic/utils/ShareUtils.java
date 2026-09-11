package com.treasure.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.treasure.basic.ContextHolder;
import com.treasure.basic.constant.ConstantUtil;

/**
 * date:2021/5/25
 * author:李超(licha)
 * function:
 */
public class ShareUtils {
    public static final String NAME = "ATB";


    /**
     * 存储String类型的值
     * @param mContext this
     * @param key      key值
     * @param value    要存储的String值
     */
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 获取String类型的值
     * @param mContext this
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static String getString(Context mContext, String key, String defValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }

    public static String getDefaultString(String key) {
        SharedPreferences sharedPreferences = ContextHolder.INSTANCE.app().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


    /**
     * 存储Int类型的值
     * @param mContext this
     * @param key      key
     * @param value    要存储的Int值
     */
    public static void putInt(Context mContext, String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).commit();
    }


    /**
     * 获取Int类型的值
     * @param mContext this
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static int getInt(Context mContext, String key, int defValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defValue);
    }
    /**
     * 存储Long类型的值
     * @param mContext this
     * @param key      key
     * @param value    要存储的Long值
     */
    public static void putLong(Context mContext, String key, long value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 获取Long类型的值
     * @param mContext this
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static long getLong(Context mContext, String key, long defValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defValue);
    }


    /**
     * 存储Boolean类型的值
     * @param mContext this
     * @param key      key
     * @param value    要存储Boolean值
     */
    public static void putBoolean(Context mContext, String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取Boolean类型的值
     * @param mContext this
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(Context mContext, String key, Boolean defValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }

    //删除 单个 key
    public static void deleShare(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).commit();
    }

    //删除全部 key
    public static void deleAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static String getSpString(String key) {
        Context context = ContextHolder.INSTANCE.app();
        if (context == null) return "";
        return ShareUtils.getString(context, key, "");
    }

    public static void putStringCurUser(String key, String value) {
        Context context = ContextHolder.INSTANCE.app();
        String tempUid = ShareUtils.getString(context, ConstantUtil.TEMP_USER_ID, "");
        ShareUtils.putString(context, tempUid + "-" + key, value);
    }

    public static String getStringCurUser(String key, String defValue) {
        Context context = ContextHolder.INSTANCE.app();
        String tempUid = ShareUtils.getString(context, ConstantUtil.TEMP_USER_ID, "");
        return ShareUtils.getString(context, tempUid + "-" + key, defValue);
    }

    public static void putBooleanCurUser(String key, boolean value) {
        Context context = ContextHolder.INSTANCE.app();
        String tempUid = ShareUtils.getString(context, ConstantUtil.TEMP_USER_ID, "");
        ShareUtils.putBoolean(context, tempUid + "-" + key, value);
    }

    public static boolean getBooleanCurUser(String key, boolean defValue) {
        Context context = ContextHolder.INSTANCE.app();
        String tempUid = ShareUtils.getString(context, ConstantUtil.TEMP_USER_ID, "");
        return ShareUtils.getBoolean(context, tempUid + "-" + key, defValue);
    }
}
