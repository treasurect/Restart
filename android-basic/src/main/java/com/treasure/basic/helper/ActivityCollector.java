package com.treasure.basic.helper;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * date:2021/8/23
 * author:李超(licha)
 * function:
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static Activity getActivity(String name) {
        for (Activity activity : activities) {
            String substring = activity.toString().substring(activity.toString().lastIndexOf(".") + 1, activity.toString().indexOf("@"));
            if (!activity.isFinishing() && substring.equals(name)) {
                return activity;
            }
        }
        return null;
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
    public static void finish(String name) {
        for (Activity activity : activities) {
            String substring = activity.toString().substring(activity.toString().lastIndexOf(".") + 1, activity.toString().indexOf("@"));
            if (!activity.isFinishing() && substring.equals(name)) {
                activity.finish();
            }
        }
    }

}
