package com.treasure.basic.ui.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.treasure.basic.R

class EditPhoneNumberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val tvAreaCode: TextView
    val etPhone: EditText

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_phone_input, this, true)

        tvAreaCode = findViewById(R.id.tv_area_code)
        etPhone = findViewById(R.id.et_phone)

        // 默认区号 +86
        tvAreaCode.text = "+86"
    }

    fun getPhone(): String = etPhone.text.toString().trim()

    fun setAreaCode(code: String) {
        tvAreaCode.text = code
    }

    fun getAreaCode(): String = tvAreaCode.text.toString()

    fun setHint(text: String) {
        etPhone.hint = text
    }

    fun setPhone(phone: String) {
        etPhone.setText(phone)
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        etPhone.addTextChangedListener(watcher)
    }
}
