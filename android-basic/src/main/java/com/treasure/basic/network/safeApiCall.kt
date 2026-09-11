package com.treasure.basic.network

import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by treasure_ct on 2026/03/09
 * Description:
 */
inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> BaseResponse<T>
): Flow<BaseResponse<T>> = flow {
    try {
        emit(apiCall())
    } catch (e: HttpException) {
        emit(BaseResponse(e.statusCode, e.message ?: "网络异常", false))
    } catch (e: Throwable) {
        emit(BaseResponse(-1, e.message ?: "网络异常", false))
    }
}.flowOn(Dispatchers.IO)