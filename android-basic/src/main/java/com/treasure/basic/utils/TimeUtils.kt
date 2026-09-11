package com.treasure.basic.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {

    const val FORMAT_FULL = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val FORMAT_TIME = "HH:mm:ss"

    fun timestampToStr(timestamp: Long, pattern: String = FORMAT_FULL): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun strToTimestamp(timeStr: String, pattern: String = FORMAT_FULL): Long {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.parse(timeStr)?.time ?: 0
    }

    fun dateToStr(date: Date, pattern: String = FORMAT_FULL): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)
    }

    fun strToDate(dateStr: String, pattern: String = FORMAT_FULL): Date? {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    fun timestampToLocalStr(timestamp: Long, pattern: String = FORMAT_FULL): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochMilli(timestamp)
            val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } else {
            timestampToStr(timestamp, pattern)
        }
    }

    fun strToLocalDateTime(str: String, pattern: String = FORMAT_FULL): LocalDateTime? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val formatter = DateTimeFormatter.ofPattern(pattern)
                LocalDateTime.parse(str, formatter)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun localDateTimeToStr(localDateTime: LocalDateTime?, pattern: String = FORMAT_FULL): String {
        return if (localDateTime != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            localDateTime.format(formatter)
        } else {
            ""
        }
    }
    // endregion

    // region --- Today / Yesterday / Tomorrow ---
    fun getToday(pattern: String = FORMAT_DATE): String {
        val calendar = Calendar.getInstance()
        return dateToStr(calendar.time, pattern)
    }

    fun getYesterday(pattern: String = FORMAT_DATE): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return dateToStr(calendar.time, pattern)
    }

    fun getTomorrow(pattern: String = FORMAT_DATE): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        return dateToStr(calendar.time, pattern)
    }
    // endregion

    // region --- 时间差计算 ---
    fun getDiffInMinutes(startTime: Long, endTime: Long): Long {
        return TimeUnit.MILLISECONDS.toMinutes(endTime - startTime)
    }

    fun getDiffInHours(startTime: Long, endTime: Long): Long {
        return TimeUnit.MILLISECONDS.toHours(endTime - startTime)
    }

    fun getDiffInDays(startTime: Long, endTime: Long): Long {
        return TimeUnit.MILLISECONDS.toDays(endTime - startTime)
    }

    fun getDiffDescription(startTime: Long, endTime: Long): String {
        val minutes = getDiffInMinutes(startTime, endTime)
        return when {
            minutes < 60 -> "$minutes 分钟前"
            minutes < 1440 -> "${minutes / 60} 小时前"
            else -> "${minutes / 60 / 24} 天前"
        }
    }
    // endregion

    fun now(): String = timestampToStr(System.currentTimeMillis())

    fun nowMillis(): Long = System.currentTimeMillis()

    fun getCurrentFormatTime(): String {
        return getCurrentFormatTime(FORMAT_FULL)
    }

    fun getCurrentFormatTime(pattern: String = FORMAT_FULL): String {
        return timestampToStr(nowMillis(), pattern)
    }
}
