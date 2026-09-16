package com.treasure.restart.network.repository

import com.treasure.basic.network.RetrofitClient
import com.treasure.basic.network.safeApiCall
import com.treasure.restart.network.api.TestApi

class TestRepo {
    private val api = RetrofitClient.getInstance().create(TestApi::class.java)

    suspend fun getWeeklyReportList(page: Int, pageSize: Int) =
        safeApiCall { api.getWeeklyReportList(page, pageSize) }
}