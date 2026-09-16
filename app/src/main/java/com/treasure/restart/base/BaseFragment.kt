package com.treasure.restart.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment : Fragment() {

    protected fun <T : ViewModel> viewModelOf(modelClass: Class<T>): T {
        return ViewModelProvider(this)[modelClass]
    }
}
