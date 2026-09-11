package com.treasure.basic.network

import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


/**
 * Date:2025/7/15
 * Author: treasure_ct
 * function:
 */
class BaseResponseAdapter<T>(
    private val delegateAdapter: TypeAdapter<BaseResponse<T>>,
    private val dataAdapter: TypeAdapter<T>
) : TypeAdapter<BaseResponse<T>>() {

    override fun write(out: JsonWriter?, value: BaseResponse<T>?) {
        delegateAdapter.write(out, value)
    }

    override fun read(reader: JsonReader): BaseResponse<T> {
        val parser = JsonParser()
        val jsonElement = parser.parse(reader)
        val jsonObject = jsonElement.asJsonObject

        // 反序列化data字段
        val dataElement = jsonObject.get("data")
        val data: T? = if (dataElement != null && !dataElement.isJsonNull) {
            dataAdapter.fromJsonTree(dataElement)
        } else {
            null
        }

        // 先反序列化BaseResponse（不含data）
        val baseResponse = delegateAdapter.fromJsonTree(jsonObject)

        // 手动把泛型data赋值回BaseResponse
        return baseResponse.copy(data = data)
    }
}
