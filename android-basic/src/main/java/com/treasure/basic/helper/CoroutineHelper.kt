package com.treasure.basic.helper

import android.os.Looper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object CoroutineHelper {

    // === Job 缓存，用于取消协程 ===
    private val jobMap: ConcurrentHashMap<String, Job> = ConcurrentHashMap()

    // === 全局异常处理器（可统一打印/上传异常信息）===
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        // 可扩展到 Bugly/Crashlytics
    }

    // === 基础 Job，生命周期由 App 进程控制 ===
    private val rootJob = SupervisorJob()

    // === 多调度器 Scope 支持 ===
    val scopeDefault = CoroutineScope(Dispatchers.Default + rootJob + exceptionHandler)
    val scopeIO = CoroutineScope(Dispatchers.IO + rootJob + exceptionHandler)
    val scopeMain = CoroutineScope(Dispatchers.Main + rootJob + exceptionHandler)
    val scopeUnconfined = CoroutineScope(Dispatchers.Unconfined + rootJob + exceptionHandler)


    // === 启动 Default 协程 ===
    fun launchOnDefault(tag: String? = null, block: suspend CoroutineScope.() -> Unit): Job {
        val job = scopeDefault.launch { block() }
        tag?.let { jobMap[it] = job }
        return job
    }

    // === 启动 IO 协程 ===
    fun launchOnIO(tag: String? = null, block: suspend CoroutineScope.() -> Unit): Job {
        val job = scopeIO.launch { block() }
        tag?.let { jobMap[it] = job }
        return job
    }

    // === 启动 Main 协程 ===
    fun launchOnMain(tag: String? = null, block: suspend CoroutineScope.() -> Unit): Job {
        val job = scopeMain.launch { block() }
        tag?.let { jobMap[it] = job }
        return job
    }

    // === 自定义 CoroutineScope 启动协程 ===
    fun launchCustomScope(
        scope: CoroutineScope, tag: String? = null, block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = scope.launch { block() }
        tag?.let { jobMap[it] = job }
        return job
    }

    // === 取消指定 Tag 的 Job ===
    fun cancel(tag: String) {
        jobMap[tag]?.cancel()
        jobMap.remove(tag)
    }

    // === 取消所有 Job，但保留 Scope 本身 ===
    fun cancelAllJobs() {
        jobMap.forEach { (_, job) -> job.cancel() }
        jobMap.clear()
    }

    // === 销毁所有协程域（仅在彻底退出 App 时调用）===
    fun destroy() {
        rootJob.cancel()
        jobMap.clear()
    }

    // === 是否主线程 ===
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    // === 自动线程切换封装 ===
    suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO) { block(this) }
    }

    suspend fun <T> withMain(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.Main) { block(this) }
    }

    suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.Default) { block(this) }
    }
}
