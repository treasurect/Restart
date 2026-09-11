package com.treasure.basic.network

/**
 * Date:2025/7/15
 * Author: treasure_ct
 * function:
 */
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class BaseResponseTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType

        if (rawType != BaseResponse::class.java) {
            return null
        }

        val parameterizedType = type.type as? ParameterizedType
            ?: throw IllegalArgumentException("BaseResponse must be parameterized")

        val actualType = parameterizedType.actualTypeArguments[0]
        val dataAdapter = gson.getAdapter(TypeToken.get(actualType))

        val delegateAdapter = gson.getDelegateAdapter(this, type)

        // 强制转换时先声明一个变量，避免类型推断问题
        @Suppress("UNCHECKED_CAST")
        val adapter = BaseResponseAdapter(delegateAdapter as TypeAdapter<BaseResponse<Any>>, dataAdapter as TypeAdapter<Any>)

        return adapter as TypeAdapter<T>
    }


}
