package com.treasure.restart.network.api

import com.treasure.restart.bean.LoginCodeBean
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("lingxi-auth/oauth/token")
    suspend fun actionLoginCode(
        @Query("sms_phone") sms_phone: String?,
        @Query("sms_id") sms_id: String?,
        @Query("grant_type") grantType: String?,
        @Query("type") type: String?,
        @Query("scope") scope: String?,
        @Query("sms_value") sms_value: String?,
        @Query("userSourceType") userSourceType: Int
    ): LoginCodeBean

}