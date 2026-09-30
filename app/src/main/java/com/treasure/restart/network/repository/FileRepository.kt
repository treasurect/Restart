package com.treasure.restart.network.repository

import com.treasure.basic.network.RetrofitClient
import com.treasure.basic.network.safeApiCall
import com.treasure.restart.network.api.FileApi
import okhttp3.MultipartBody

class FileRepository {
    private val api = RetrofitClient.getInstance().create(FileApi::class.java)

    suspend fun uploadFiles(parts: List<MultipartBody.Part>) = safeApiCall {
        api.uploadFiles(parts)
    }
}