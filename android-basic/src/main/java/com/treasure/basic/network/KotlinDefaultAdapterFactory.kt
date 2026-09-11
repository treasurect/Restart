package com.treasure.basic.network

/**
 * Created by treasure_ct on 2025/08/06
 * Description:
 */

import android.text.TextUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class KotlinDefaultAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        val kClass = rawType.kotlin

        if (!kClass.isData) return null

        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader): T {
                val jsonElement = JsonParser().parse(`in`)

                if (!jsonElement.isJsonObject) {
                    return delegate.fromJsonTree(jsonElement)
                }

                val jsonObject = jsonElement.asJsonObject
                val constructor = kClass.primaryConstructor ?: return delegate.fromJsonTree(jsonElement)
                constructor.isAccessible = true

                val args = mutableMapOf<KParameter, Any?>()

                constructor.parameters.forEach { param ->
                    val name = checkName(param) ?: param.name
                    if (name == null) return@forEach

                    val typeClass = param.type.jvmErasure

                    val jsonValue = if (jsonObject.has(name) && !jsonObject.get(name).isJsonNull) {
                        gson.fromJson(jsonObject.get(name), typeClass.java)
                    } else {
                        if (param.isOptional) return@forEach
                        getSafeDefault(typeClass)
                    }
                    args[param] = jsonValue
                }

                val instance = constructor.callBy(args)

                // 调试输出，确认实例类型
                println("Created instance of type: ${instance?.javaClass}")

                @Suppress("UNCHECKED_CAST")
                return instance as T
            }

            private fun checkName(param: KParameter): String? {
                // 这里是你自定义的字段映射，可以扩展
                return if (param.name == "isSuccess") "success" else param.name
            }
        }
    }

    private fun getSafeDefault(type: KClass<*>): Any? = when (type) {
        String::class -> ""
        Int::class -> 0
        Long::class -> 0L
        Float::class -> 0f
        Double::class -> 0.0
        Boolean::class -> false
        else -> null
    }
}


