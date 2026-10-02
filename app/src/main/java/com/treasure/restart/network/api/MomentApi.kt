package com.treasure.restart.network.api

import com.treasure.basic.network.BaseResponse
import com.treasure.restart.bean.MomentListResponse
import com.treasure.restart.bean.MomentPublishRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MomentApi {
    @POST("api/moments/publish")
    suspend fun publishMoment(@Body request: MomentPublishRequest): BaseResponse<Long>

    @GET("api/moments/list")
    suspend fun getMomentList(
        @Query("page") page: Int, @Query("size") size: Int
    ): BaseResponse<MomentListResponse>
}