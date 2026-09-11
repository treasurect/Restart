package com.treasure.basic.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date:2025/7/14
 * Author: treasure_ct
 * function:
 */
class DebounceCallback(
    private val scope: CoroutineScope,
    private val delayMillis: Long = 1000L
) {
    private var debounceJob: Job? = null

    fun submit(event: String, onDebounced: (String) -> Unit) {
        debounceJob?.cancel()  // 取消上次未执行的任务

        debounceJob = scope.launch {
            delay(delayMillis)
            onDebounced(event) // 延迟后执行
        }
    }
}