package com.treasure.basic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log

object ContextHolder {
    @Volatile
    private var appContext: Context? = null

    /**
     * 推荐在 Application 中主动初始化
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /**
     * 全局获取 Context。优先使用主动初始化的 Context，若未初始化则尝试通过反射获取。
     */
    fun app(): Context {
        return appContext ?: kotlin.run {
            Log.e("treasure_ct", "need init application context")
            getByReflection()
        }
    }

    /**
     * 尝试通过反射获取 Application 实例
     */
    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun getByReflection(): Context {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val currentThreadMethod =
                activityThreadClass.getDeclaredMethod("currentActivityThread")
            currentThreadMethod.isAccessible = true
            val activityThread = currentThreadMethod.invoke(null)

            val appField = activityThreadClass.getDeclaredField("mInitialApplication")
            appField.isAccessible = true
            val app = appField.get(activityThread) as Application

            appContext = app.applicationContext
            appContext!!
        } catch (e: Exception) {
            throw IllegalStateException(
                "GlobalContext is not initialized and reflection failed.",
                e
            )
        }
    }
}
