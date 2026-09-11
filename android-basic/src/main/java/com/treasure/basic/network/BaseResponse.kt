package com.treasure.basic.network

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("code")
    var code: Int = 0,

    @SerializedName("msg")
    var msg: String? = "",

    @SerializedName("success")
    var isSuccess: Boolean = false,

    @SerializedName("data")
    var data: T? = null
) {
    //fun isSuccess(): Boolean = code == 200

    fun getOrNull(): T? = if (isSuccess) data else null

    fun getOrThrow(): T {
        if (isSuccess && data != null) {
            return data!!
        } else {
            throw ApiException(code, msg ?: "")
        }
    }
}

class ApiException(val code: Int, message: String) : Exception("[$code] $message")
