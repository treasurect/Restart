package com.treasure.restart.network.api

import com.treasure.basic.network.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {
    @Multipart
    @POST("api/file/upload")
    suspend fun uploadFiles(
        @Part files: List<MultipartBody.Part>
    ): BaseResponse<List<String>>
}