package com.treasure.restart.network.repository

import com.treasure.basic.AppConfig
import com.treasure.basic.network.RetrofitClient
import com.treasure.restart.network.api.UserApi
import kotlinx.coroutines.flow.flow

class UserRepo {
    private val api = RetrofitClient.getInstance().create(UserApi::class.java)

    suspend fun actionLoginCode(phone: String?, id: String?, value: String?) = flow {
        emit(
            api.actionLoginCode(phone, id, "sms", "account", "all", value, AppConfig.userSourceType)
        )
    }
}