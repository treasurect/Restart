package com.treasure.basic.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treasure.basic.utils.ToastUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by treasure_ct on 2025/07/09
 * Description:
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val toast = MutableSharedFlow<String?>()
    val toastEvent: SharedFlow<String?> = toast
    init {
        viewModelScope.launch {
            toastEvent.collectLatest {
                ToastUtils.show(it ?: "")
            }
        }
    }
}