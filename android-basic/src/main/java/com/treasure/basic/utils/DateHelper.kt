package com.treasure.basic.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateHelper {
    fun formatDateMd(date: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val output = SimpleDateFormat("MM-dd", Locale.getDefault())
            output.format(input.parse(date)!!)
        } catch (e: Exception) {
            ""
        }
    }
}