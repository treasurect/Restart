package com.treasure.basic.utils

import android.text.TextUtils

/**
 * 检查手机号
 * @author wct
 * @date 2024.12.26
 */
object EncryptUtils {
    /**
     * 移动号码 & 座机号码加密
     * @param this
     * @param isMobile
     * @return
     */
    fun String.encryptPhoneNumber(isMobile: Boolean): String {
        if (TextUtils.isEmpty(this)) return this
        if (isMobile) {
            if (this.length != 11 || !this.matches("^1[3-9]\\d{9}$".toRegex())) return this
            return this.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        } else {
            if (!this.matches("\\d{3,4}-\\d{7,8}".toRegex())) return this
            return this.replace("(\\d{3,4}-\\d{3,4})\\d{4}".toRegex(), "$1****")
        }
    }

    /**
     * 部分遮掩姓名
     * @param this 输入的姓名
     * @return 加密后的姓名
     */
    fun String.encryptName(): String {
        if (TextUtils.isEmpty(this)) return this
        if (this.length == 2) return "*" + this[1]
        val maskedName = StringBuilder()
        maskedName.append(this[0])
        for (i in 1 until this.length-1) {
            maskedName.append("*")
        }
        maskedName.append(this[length - 1])
        return maskedName.toString()
    }

    fun String.encryptIdNumber(): String {
        if (this.length < 8) {
            return this // 长度不足不加密
        }
        val length = this.length
        val prefix = this.substring(0, 4)
        val suffix = this.substring(length - 4)
        val masked = StringBuilder()
        for (i in 0 until length - 8) {
            masked.append("*")
        }
        return prefix + masked + suffix
    }
}
