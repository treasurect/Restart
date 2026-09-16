package com.treasure.restart.bean


data class WeeklyReportList(
    val records: List<WeeklyReportItem> = emptyList(),
    val total: Int = 0,
    val size: Int = 0,
    val current: Int = 0,
    val pages: Int = 0
)
data class WeeklyReportItem(
    val id: Long = 0,
    val reportTitle: String = "",
    val summary: String = "",
    val reportDate: String = "",
    val weekDay: String = "",
    val pushTags: String = "",
    val htmlLink: String = "",
)