package com.treasure.basic.utils;

import android.view.View;

public class DoubleClickUtils {
    // 默认最小点击间隔时间（单位：毫秒）
    private static final long DEFAULT_INTERVAL = 500;
    private static long lastClickTime = 0;

    /**
     * 是否是快速点击（全局判断，默认800ms）
     */
    public static boolean isFastClick() {
        return isFastClick(DEFAULT_INTERVAL);
    }

    /**
     * 是否是快速点击
     * @param intervalMillis 允许的最小点击间隔时间
     */
    public static boolean isFastClick(long intervalMillis) {
        long currentTime = System.currentTimeMillis();
        boolean isFast = (currentTime - lastClickTime) < intervalMillis;
        lastClickTime = currentTime;
        return isFast;
    }


    // 给每个 View 设置独立点击时间的 key（用一个常量 ID）
    private static final int KEY_LAST_CLICK_TIME = -10001;

    /**
     * 接口：安全点击回调
     */
    public interface OnSafeClickCallback {
        void onSafeClick(View v);
    }

    /**
     * 给 View 设置防抖点击事件
     * @param view     需要设置点击事件的 View
     * @param interval 点击间隔（毫秒）
     * @param callback 合法点击时的回调
     */
    public static void withSafeClick(View view, long interval, OnSafeClickCallback callback) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag(KEY_LAST_CLICK_TIME);
                long lastClickTime = tag instanceof Long ? (Long) tag : 0L;
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime >= interval) {
                    v.setTag(KEY_LAST_CLICK_TIME, currentTime);
                    callback.onSafeClick(v);
                }
            }
        });
    }

    public static void withSafeClick(View view, OnSafeClickCallback callback) {
        withSafeClick(view, DEFAULT_INTERVAL, callback);
    }
}

