package com.treasure.restart.helper

import android.content.Context
import com.treasure.basic.ContextHolder
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.treasure.basic.SharedKey
import com.treasure.basic.helper.SharePreferenceManager
import com.treasure.basic.utils.ToastUtils
import com.treasure.restart.network.repository.UserResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

object LoginManager {

    private const val PREFS_NAME = "restart_login"


    fun isLoggedIn(): Boolean {
        return SharePreferenceManager.getBoolean(SharedKey.KEY_LOGGED_IN)
    }

    fun login(context: Context?, loginType: LoginType, map: HashMap<String, Any> = HashMap()) {
        when (loginType) {
            LoginType.TYPE_PWD -> {
                (context as? FragmentActivity)?.lifecycleScope?.launch {
                    val phone = "${map["phone"] ?: ""}"
                    val value = "${map["pwd"] ?: ""}"
                    UserResponse().actionLoginCode(phone, value).collectLatest {
                        if (it.code == 200) {
                            ToastUtils.show(it.msg ?: "")
                            SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, true)
                            SharePreferenceManager.putString(SharedKey.ACCESS_TOKEN, it.data?.token ?: "")
                            delay(500.milliseconds)
                            AppRestartHelper.restart(context)
                        } else {
                            ToastUtils.show("登录失败：${it.msg}")
                        }
                    }
                }
            }

            LoginType.TYPE_VERIFY_CODE -> {
                (context as? FragmentActivity)?.lifecycleScope?.launch {
                    val phone = "${map["phone"] ?: ""}"
                    UserResponse().actionLoginCode(phone, "123456").collectLatest {
                        if (it.code == 200) {
                            ToastUtils.show(it.msg ?: "")
                            SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, true)
                            delay(500.milliseconds)
                            AppRestartHelper.restart(context)
                        } else {
                            ToastUtils.show("登录失败：${it.msg}")
                        }
                    }
                }
            }

            LoginType.TYPE_ONE_KEY -> {
                ToastUtils.show("登录成功")
                SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, true)
                AppRestartHelper.restart(ContextHolder.app())
            }

            LoginType.TYPE_WECHAT -> {
                ToastUtils.show("登录成功")
                SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, true)
                AppRestartHelper.restart(ContextHolder.app())
                AppRestartHelper.restart(ContextHolder.app())
            }

            LoginType.TYPE_ALIPAY -> {
                ToastUtils.show("登录成功")
                SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, true)
                AppRestartHelper.restart(ContextHolder.app())
            }
        }


    }

    fun logout() {
        SharePreferenceManager.putBoolean(SharedKey.KEY_LOGGED_IN, false)
        SharePreferenceManager.remove(SharedKey.ACCESS_TOKEN)
    }
}

enum class LoginType(type: Int) {
    TYPE_PWD(1),
    TYPE_VERIFY_CODE(2),
    TYPE_ONE_KEY(3),
    TYPE_WECHAT(4),
    TYPE_ALIPAY(5),
}
