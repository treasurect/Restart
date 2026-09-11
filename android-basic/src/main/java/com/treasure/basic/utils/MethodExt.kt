package com.treasure.basic.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.treasure.basic.bean.AdvancedSearchBean
import java.io.Serializable

/**
 * Created by treasure_ct on 2026/03/05
 * Description:
 */
object MethodExt {
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? {
        return extras?.parcelable(key)
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION") getParcelable(key)
        }
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? {
        return extras?.serializable(key)
    }

    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? {
        return (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key)
        }) as T?
    }

}