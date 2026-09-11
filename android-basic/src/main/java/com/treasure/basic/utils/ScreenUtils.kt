package com.treasure.basic.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.WindowInsets

object ScreenUtils {

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getDensityDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    fun dp2px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }

    fun px2dp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun sp2px(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics
        ).toInt()
    }

    fun px2sp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.scaledDensity
    }

    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun getRealScreenSize(activity: Activity): Point {
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = activity.windowManager.currentWindowMetrics.bounds
            size.x = bounds.width()
            size.y = bounds.height()
        } else {
            val display = activity.windowManager.defaultDisplay
            display.getRealSize(size)
        }
        return size
    }

    fun getAppUsableScreenSize(activity: Activity): Point {
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.windowManager.currentWindowMetrics.windowInsets
                .getInsets(WindowInsets.Type.systemBars())
            val bounds = activity.windowManager.currentWindowMetrics.bounds
            size.x = bounds.width() - insets.left - insets.right
            size.y = bounds.height() - insets.top - insets.bottom
        } else {
            val display = activity.windowManager.defaultDisplay
            display.getSize(size)
        }
        return size
    }
}
