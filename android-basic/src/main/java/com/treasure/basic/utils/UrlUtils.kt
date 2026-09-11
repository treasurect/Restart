package com.treasure.basic.utils

/**
 * Created by treasure_ct on 2025/08/21
 * Description:
 */
import android.net.Uri
import java.net.URLDecoder
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset

object UrlUtils {

    private val UTF8 = Charset.forName("UTF-8")

    /**
     * 获取 URL 中的某个参数
     * @param urlStr URL 字符串
     * @param key 参数名
     * @return 参数值，如果不存在返回 null
     */
    fun getQueryParameters(url: String): Map<String, String> {
        return try {
            val encode = URLEncoder.encode(url, "UTF-8")
            val uri = Uri.parse(encode)
            val queryMap = mutableMapOf<String, String>()

            uri.queryParameterNames.forEach { key ->
                uri.getQueryParameter(key)?.let { value ->
                    queryMap[key] = value
                }
            }
            queryMap
        } catch (e: Exception) {
            emptyMap()
        }
    }

    /**
     * 获取 URL 中的所有参数
     * @param urlStr URL 字符串
     * @return Map<String, String> 参数键值对
     */
    fun getQueryParams(urlStr: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        try {
            val uri = URI(urlStr)
            val query = uri.rawQuery ?: return result
            val pairs = query.split("&")
            for (pair in pairs) {
                val idx = pair.indexOf("=")
                if (idx > 0) {
                    val key = URLDecoder.decode(pair.substring(0, idx), UTF8.name())
                    val value = URLDecoder.decode(pair.substring(idx + 1), UTF8.name())
                    result[key] = value
                }
            }
        } catch (_: Exception) { }
        return result
    }
}
