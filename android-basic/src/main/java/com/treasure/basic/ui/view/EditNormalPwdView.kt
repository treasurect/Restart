package com.treasure.basic.ui.view

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditNormalPwdView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        // 设置为密码模式
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // 设置密码显示为圆点（系统默认字符为 •）
        transformationMethod = PasswordTransformationMethod.getInstance()

        // 去除背景（可自定义）
        background = null

        // 让光标位于末尾
        setSelection(text?.length ?: 0)
    }

    fun setOnCompleteListener(length: Int = 6, listener: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString() ?: ""
                if (input.length == length) {
                    listener.invoke(input)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}
