package com.treasure.restart

import android.content.Context
import com.treasure.basic.AppEnvironment
import com.treasure.basic.EnvManager
import com.treasure.basic.IEnvironmentProvider

class AppEnvProvider(context: Context, currentEnv: AppEnvironment) : IEnvironmentProvider {
    
    init {
        EnvManager.setEnvironment(context, currentEnv)
    }
    
    override fun getBaseUrl(env: AppEnvironment): String = when (env) {
        AppEnvironment.DEV -> "http://chaolianzhaoshang-gateway-base.zhaoshangpi.lxdev.cn/"
        AppEnvironment.TEST -> "https://chaolianzhaoshang-gateway-base.zhaoshangpi.lingxitest.com/"
        AppEnvironment.PRE -> "https://api-zsp.lingxidata.cn/" // TODO: 配置预发环境URL
        AppEnvironment.PROD -> "https://api-zsp.lingxidata.cn/"
    }

    override fun isLogEnable(env: AppEnvironment): Boolean =
        when (env) {
            AppEnvironment.PROD -> BuildConfig.DEBUG
            else -> true
        }
}