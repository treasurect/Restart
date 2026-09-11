package com.treasure.basic.constant

import com.treasure.basic.AppConfig

/**
 * Created by treasure_ct on 2025/08/01
 * Description:
 */
object ThirdPartyKey {

    val ONEKEYCODE: String //z4WjTwa0  一键登录
        get() = if (AppConfig.isParkApp) "h4uT8aDn" else "mGYFkNKE"

    //测试 wx50115919920a2cad //生产 wxa592555a048044e5
     val WXAPP_ID: String
        get() = if (AppConfig.isParkApp) "wx48e0eff0ad4ffede" else "wx0ee91f97ef032e32"

    val UMENG_APPKEY: String
        get() = if (AppConfig.isParkApp) "689d5405ec2b5b6f881d0419" else "689d54d4e563686f427bdab9"

    val BUGLY_APPID: String
        get() = if (AppConfig.isParkApp) "26c5cda15a" else "5644ffb489"

    val BUGLY_APPKEY: String
        get() = if (AppConfig.isParkApp) "ca4d5997-51a0-4634-bc0a-163da531029d" else "bdd4adb1-3f91-4360-a1b3-faf176ffbf39"

    val JIGUANG_APPKEY: String
        get() = if (AppConfig.isParkApp) "52d4d68cd128f4234af926bc" else "aaef874c10bcf0631b00f6b0"
}