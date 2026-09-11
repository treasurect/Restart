package com.treasure.basic

import android.content.Context
import com.treasure.basic.helper.LogHelper
import com.treasure.basic.utils.ToastUtils

object AppInitializer {

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true
        ContextHolder.init(context)
        LogHelper.init(context)
        ToastUtils.init(context)
//        if (BuildConfig.DEBUG) {
//            ARouter.openLog()
//            ARouter.openDebug()
//        }
//        ARouter.init(context as Application)
    }
}