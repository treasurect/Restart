package com.treasure.basic

interface AppConfig {
    companion object {
        //        var baseUrl: String = "https://api.example.com"
//        var enableLog: Boolean = true
//        var currentUserId: String? = null
//        var versionCode: Int = 0
        var VERSION_NAME: String = ""
        var isDebug: Boolean = false
        var userSourceType:Int = 1

        val isParkApp:Boolean
            get() = userSourceType == 1

        val isPartnerApp:Boolean
            get() = userSourceType == 2
    }

    //适用于隐私政策同意之后的 初始化
    fun processBusinessInit()
}

object AppBridge {
    var config: AppConfig? = null
}