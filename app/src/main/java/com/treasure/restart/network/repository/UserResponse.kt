package com.treasure.restart.network.repository

import com.treasure.basic.network.RetrofitClient
import com.treasure.basic.network.safeApiCall
import com.treasure.basic.utils.Ext.toJsonBody
import com.treasure.restart.network.api.UserApi

class UserResponse {
    private val api = RetrofitClient.getInstance().create(UserApi::class.java)

    suspend fun actionLoginCode(phone: String, pwd: String) =
        safeApiCall {
            api.actionLoginPwd(
                hashMapOf<String, Any>(
                    "username" to phone,
                    "password" to pwd,
                    "nickname" to phone
                ).toJsonBody()
            )
        }
}
