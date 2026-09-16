package com.treasure.restart.helper

import android.content.Context
import com.treasure.basic.ContextHolder
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.treasure.basic.network.ApiResult
import com.treasure.basic.network.asResult
import com.treasure.basic.utils.ToastUtils
import com.treasure.restart.network.repository.UserRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

object LoginManager {

    private const val PREFS_NAME = "restart_login"
    private const val KEY_LOGGED_IN = "logged_in"

    fun isLoggedIn(): Boolean {
        val context = ContextHolder.app()
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED_IN, true)
    }

    fun login(context: Context?, loginType: LoginType, map: HashMap<String, Any> = HashMap()) {
        when(loginType) {
            LoginType.TYPE_PWD ->{
                (context as? FragmentActivity)?.lifecycleScope?.launch {
                    val phone = "${map["phone"] ?: ""}"
                    val value = "${map["pwd"] ?: ""}"
                    UserRepo().actionLoginCode(phone, "", value).collectLatest {
                        if (it.error == null) {
                            ToastUtils.show("登录成功")
                            ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                                putBoolean(KEY_LOGGED_IN, true)
                            }
                            delay(500.milliseconds)
                            AppRestartHelper.restart(context)
                        } else {
                            ToastUtils.show("登录失败：${it.error}")
                        }
                    }
                }
            }
            LoginType.TYPE_VERIFY_CODE ->{
                (context as? FragmentActivity)?.lifecycleScope?.launch {
                    val phone = "${map["phone"] ?: ""}"
                    UserRepo().actionLoginCode(phone, "", "666").collectLatest {
                        if (it.error == null) {
                            ToastUtils.show("登录成功")
                            ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                                putBoolean(KEY_LOGGED_IN, true)
                            }
                            delay(500.milliseconds)
                            AppRestartHelper.restart(context)
                        } else {
                            ToastUtils.show("登录失败：${it.error}")
                        }
                    }
                }
            }
            LoginType.TYPE_ONE_KEY ->{
                ToastUtils.show("登录成功")
                ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                    putBoolean(KEY_LOGGED_IN, true)
                }
                AppRestartHelper.restart(ContextHolder.app())
            }
            LoginType.TYPE_WECHAT ->{
                ToastUtils.show("登录成功")
                ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                    putBoolean(KEY_LOGGED_IN, true)
                }
                AppRestartHelper.restart(ContextHolder.app())
            }
            LoginType.TYPE_ALIPAY ->{
                ToastUtils.show("登录成功")
                ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                    putBoolean(KEY_LOGGED_IN, true)
                }
                AppRestartHelper.restart(ContextHolder.app())
            }
        }


    }

    fun logout() {
        ContextHolder.app().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_LOGGED_IN, false)
        }
    }
}

enum class LoginType(type: Int) {
    TYPE_PWD(1),
    TYPE_VERIFY_CODE(2),
    TYPE_ONE_KEY(3),
    TYPE_WECHAT(4),
    TYPE_ALIPAY(5),
}
