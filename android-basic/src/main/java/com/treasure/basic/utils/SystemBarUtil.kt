package com.treasure.basic.utils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding


object SystemBarUtil {

    /**
     * 设置沉浸式状态栏：状态栏透明，内容侵入状态栏区域
     */
    fun immersiveStatusBar(activity: Activity, darkIcons: Boolean = true) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        setStatusBarTransparent(activity.window)
        setStatusBarIconColor(activity.window, darkIcons)
    }

    /**
     * 设置状态栏为透明
     */
    fun setStatusBarTransparent(window: Window) {
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
            } else {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    fun Activity.enableImmersiveStatusBar(isDark: Boolean = true) {
        window.statusBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isDark
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                bottom = systemBars.bottom // 调整底部 padding 避免导航栏遮挡
            )
            insets
        }
    }

    fun Activity.enableBottomStatusBar(isDark: Boolean = true){
        setWhiteStatusBarIconsDark()
    }

    fun Activity.setWhiteStatusBarIconsDark() {
        // 设置状态栏白底
        window.statusBarColor = Color.WHITE

        // 设置状态栏图标为深色（黑色），Android 6.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                bottom = systemBars.bottom // 调整底部 padding 避免导航栏遮挡
            )
            insets
        }
    }


    /**
     * 设置状态栏图标颜色：true = 黑色（浅色背景） false = 白色（深色背景）
     */
    fun setStatusBarIconColor(window: Window, isDark: Boolean) {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = isDark
    }

    /**
     * 设置状态栏颜色，并可选择是否为深色图标
     */
    fun setStatusBarColor(activity: Activity, @ColorInt color: Int, darkIcons: Boolean = true) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)
        activity.window.statusBarColor = color
        setStatusBarIconColor(activity.window, darkIcons)
    }

    /**
     * 显示状态栏
     */
    fun showStatusBar(activity: Activity) {
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.show(WindowInsetsCompat.Type.statusBars())
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar(activity: Activity) {
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    /**
     * 设置导航栏颜色，并可设置深浅图标颜色
     */
    fun setNavigationBarColor(activity: Activity, @ColorInt color: Int, darkIcons: Boolean = true) {
        activity.window.navigationBarColor = color
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.isAppearanceLightNavigationBars = darkIcons
    }

    /**
     * 设置导航栏透明
     */
    fun setNavigationBarTransparent(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 显示导航栏
     */
    fun showNavigationBar(activity: Activity) {
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.show(WindowInsetsCompat.Type.navigationBars())
    }

    /**
     * 隐藏导航栏（底部虚拟按键/手势条）
     */
    fun hideNavigationBar(activity: Activity) {
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    /**
     * 进入全屏（隐藏状态栏和导航栏）
     */
    fun enterFullScreen(activity: Activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    /**
     * 退出全屏（显示状态栏和导航栏）
     */
    fun exitFullScreen(activity: Activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.show(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * 设置状态栏和导航栏都透明，图标颜色可配置
     */
    fun setSystemBarTransparent(
        activity: Activity,
        darkStatusIcons: Boolean = true,
        darkNavIcons: Boolean = true
    ) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.apply {
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.isAppearanceLightStatusBars = darkStatusIcons
        controller.isAppearanceLightNavigationBars = darkNavIcons
    }

    /**
     * 判断当前是否为沉浸式状态
     */
    fun isImmersive(window: Window): Boolean {
        //val isDecorFits = WindowCompat.getDecorFitsSystemWindows(window)
        return ViewCompat.getRootWindowInsets(window.decorView)
            ?.isVisible(WindowInsetsCompat.Type.systemBars()) ?: true
    }

    fun Activity.setWindowAdaptation() {
        ViewCompat.setOnApplyWindowInsetsListener(this.window.decorView) { v: View?, insets: WindowInsetsCompat ->
            val statusBars: Insets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBars: Insets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val contentView = this.findViewById<View>(R.id.content)
            contentView?.setPadding(0, statusBars.top, 0, navigationBars.bottom)
            insets
        }
    }

    fun getStatusBarHeight(activity: Context): Int {
        if (activity !is Activity) return 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.windowManager.currentWindowMetrics
                .windowInsets.getInsets(WindowInsets.Type.statusBars())
            insets.top
        } else {
            val resourceId =
                activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId) else 0
        }
    }


    fun getNavigationBarHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.windowManager.currentWindowMetrics
                .windowInsets.getInsets(WindowInsets.Type.navigationBars())
            insets.bottom
        } else {
            val resourceId =
                activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId) else 0
        }
    }


    fun hasNavigationBar(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.window.insetsController?.systemBarsBehavior
            insets != null
        } else {
            val id = activity.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            id > 0 && activity.resources.getBoolean(id)
        }
    }

    fun getStatusHeightFormatH5(context: Context?): Int {
        if (context == null || context !is Activity) return 0
        val statusBarHeight = getStatusBarHeight(context)
        val radio = ScreenUtils.getScreenHeight(context).toFloat() / 896
        return if (radio == 0f) statusBarHeight else (statusBarHeight / radio).toInt()
    }

    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                if (rootView != null) {
                    rootView.fitsSystemWindows = fitSystemWindows
                }
            }
        }
    }
}
