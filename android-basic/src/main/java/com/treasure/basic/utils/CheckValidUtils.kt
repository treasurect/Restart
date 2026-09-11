package com.treasure.basic.utils

object CheckValidUtils {

    /**
     * 校验手机号（中国大陆，11 位，1 开头）
     */
    fun String?.isValidPhone(): Boolean {
        if (this.isNullOrBlank()) return false
        val regex = Regex("^1[3-9]\\d{9}$")
        return regex.matches(this)
    }
/**
 * 校验座机号格式
 */
fun String?.isValidTel(): Boolean {
    if (this.isNullOrBlank()) return false
    // 合并后的正则表达式，支持多种座机格式：
    // 1. 区号-号码格式 (如: 010-12345678)
    // 2. 纯数字格式 (如: 12345678)
    // 3. 长数字格式 (如: 12345678901)
    // 4. 400/800号码格式 (如: 400-123-4567 或 4001234567)
    val regex = Regex("^\\d{3,4}-\\d{7,8}$|^\\d{7,8}$|^\\d{10,12}$|^(400|800)(-\\d{3}-\\d{4}|\\d{7})$")
    return regex.matches(this)
}


    /**
     * 校验邮箱地址格式
     */
    fun String?.isValidEmail(): Boolean {
        if (this.isNullOrBlank()) return false
        val regex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        return regex.matches(this)
    }

    /**
     * 可选：校验密码格式（至少 6 位，可包含字母数字特殊字符）
     */
    fun String?.isValidPassword(): Boolean {
        if (this.isNullOrBlank()) return false
        return this.length >= 6
    }
}
