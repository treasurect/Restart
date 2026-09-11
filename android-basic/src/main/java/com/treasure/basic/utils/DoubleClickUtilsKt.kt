package com.treasure.basic.utils

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

object DoubleClickUtilsKt {
    fun View.setDebouncedClickListener(
        scope: CoroutineScope, intervalMs: Long = 500L, onClick: (View) -> Unit
    ) {
//        val eventChannel = Channel<View>(Channel.UNLIMITED)
//
//        setOnClickListener {
//            eventChannel.trySend(it)
//        }
//
//        scope.launch {
//            eventChannel
//                .consumeAsFlow()
//                .debounce(intervalMs)
//                .collect { view ->
//                    onClick(view)
//                }
//        }
        setDebouncedClickListener(intervalMs,onClick)
    }

    fun View.setDebouncedClickListener(onClick: (View) -> Unit) {
        setDebouncedClickListener(500, onClick)
    }

    fun View.setDebouncedClickListener(interval: Long = 500L, onClick: (View) -> Unit) {
        var lastClickTime = 0L
        setOnClickListener {
            val current = System.currentTimeMillis()
            if (current - lastClickTime > interval) {
                lastClickTime = current
                onClick(it)
            }
        }
    }
}