package com.treasure.basic

import android.content.Context
import android.preference.PreferenceManager
import com.treasure.basic.utils.ShareUtils

object EnvManager {

    private const val PREF_KEY_ENV = "current_env"
    private var currentEnv: AppEnvironment = AppEnvironment.PROD
    private var provider: IEnvironmentProvider? = null

    fun init(context: Context, provider: IEnvironmentProvider) {
        this.provider = provider
        val name = ShareUtils.getString(context, PREF_KEY_ENV, AppEnvironment.PROD.name) ?: AppEnvironment.PROD.name
        currentEnv = AppEnvironment.valueOf(name)
    }

    fun getCurrentEnv(): AppEnvironment = currentEnv

    fun setEnvironment( context: Context,env: AppEnvironment) {
        currentEnv = env
        ShareUtils.putString(context, PREF_KEY_ENV, env.name)
    }

    fun getBaseUrl(): String {
        return provider?.getBaseUrl(currentEnv)
            ?: throw IllegalStateException("EnvironmentProvider 未初始化")
    }

    fun logEnable(): Boolean = provider?.isLogEnable(currentEnv) ?: false

    fun getEnvName(): String = currentEnv.displayName
}


interface IEnvironmentProvider {
    fun getBaseUrl(env: AppEnvironment): String
    fun isLogEnable(env: AppEnvironment): Boolean
}


enum class AppEnvironment(val displayName: String) {
    DEV("开发环境"),
    TEST("测试环境"),
    PRE("预发环境"),
    PROD("生产环境")
}
