package com.treasure.restart.network.api

import com.treasure.basic.network.BaseResponse
import com.treasure.restart.bean.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("api/user/loginPwd")
    suspend fun actionLoginPwd(
        @Body request: RequestBody
    ): BaseResponse<LoginResponse>
}