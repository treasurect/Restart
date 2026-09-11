package com.treasure.basic.helper

import android.content.Context
import android.util.Log
import com.treasure.basic.EnvManager
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object LogHelper {

    private const val TAG = "LogHelper"
    private const val LOG_FOLDER_NAME = "logs"
    private const val LOG_FILE_EXTENSION = ".log"

    // 可配置的开关
    var logEnable = true
    private lateinit var logDir: File

    /**
     * 初始化日志管理器
     */
    fun init(context: Context) {
        logEnable = EnvManager.logEnable()
        logDir = File(context.getExternalFilesDir(null), LOG_FOLDER_NAME)
        if (!logDir.exists()) logDir.mkdirs()
    }

    fun d(msg: String) {
        d(msg, TAG)
    }

    fun d(msg: String, tag: String = TAG) {
        if (logEnable) {
            Log.d(tag, msg)
            writeLogToFile("DEBUG", tag, msg)
        }
    }

    fun i(msg: String) {
        if (logEnable) {
            Log.i(TAG, msg)
            writeLogToFile("INFO", TAG, msg)
        }
    }

    fun i(msg: String, tag: String = TAG) {
        if (logEnable) {
            Log.i(tag, msg)
            writeLogToFile("INFO", tag, msg)
        }
    }

    fun w(msg: String, tag: String = TAG) {
        if (logEnable) {
            Log.w(tag, msg)
            writeLogToFile("WARN", tag, msg)
        }
    }

    fun e(msg: String, tag: String = TAG, throwable: Throwable? = null) {
        if (logEnable) {
            Log.e(tag, msg, throwable)
            writeLogToFile("ERROR", tag, msg + (throwable?.stackTraceToString() ?: ""))
        }
    }

    private fun writeLogToFile(level: String, tag: String, msg: String) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val logFile = File(logDir, "${dateFormat.format(Date())}$LOG_FILE_EXTENSION")

            val timeStamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
            val fullLog = "$timeStamp [$level][$tag]: $msg\n"

            FileWriter(logFile, true).use { writer ->
                writer.append(fullLog)
            }

        } catch (e: Exception) {
            Log.e(TAG, "writeLogToFile failed: ${e.message}")
        }
    }

    /**
     * 获取日志文件列表
     */
    fun getLogFiles(): List<File> {
        return if (LogHelper::logDir.isInitialized && logDir.exists()) {
            logDir.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    /**
     * 清空日志
     */
    fun clearLogs() {
        getLogFiles().forEach { it.delete() }
    }
}
