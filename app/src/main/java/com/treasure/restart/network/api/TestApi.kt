package com.treasure.restart.network.api

import com.treasure.basic.network.BaseResponse
import com.treasure.restart.bean.WeeklyReportList
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {
    @GET("lingxi-system/weekly-report/page")
    suspend fun getWeeklyReportList(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("reportTitle") reportTitle: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): BaseResponse<WeeklyReportList>
}