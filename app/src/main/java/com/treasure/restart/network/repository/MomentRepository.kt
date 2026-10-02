package com.treasure.restart.network.repository

import com.treasure.basic.network.RetrofitClient
import com.treasure.basic.network.safeApiCall
import com.treasure.restart.bean.MomentPublishRequest
import com.treasure.restart.network.api.FileApi
import com.treasure.restart.network.api.MomentApi
import okhttp3.MultipartBody

class MomentRepository {
    private val api = RetrofitClient.getInstance().create(MomentApi::class.java)

    suspend fun publishMoment(request: MomentPublishRequest) = safeApiCall {
        api.publishMoment(request)
    }

    suspend fun getMomentList(page: Int, size: Int) = safeApiCall {
        api.getMomentList(page, size)
    }
}