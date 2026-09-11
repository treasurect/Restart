package com.treasure.basic.ui.view

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.treasure.basic.R
import java.util.Locale

class EditVerifyCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

     val editText: EditText
    private val sendText: TextView
    private var countDownTimer: CountDownTimer? = null
    private var totalTime = 60000L
    private var interval = 1000L

    var onSendClick: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_verify_code, this, true)

        editText = findViewById(R.id.et_code)
        sendText = findViewById(R.id.tv_send)

        sendText.setOnClickListener {
            if (sendText.isEnabled) {
                onSendClick?.invoke()
            }
        }
    }

    fun getCode(): String = editText.text.toString()

    //请求接口后调用
    fun start() {
        sendText.isEnabled = false
        countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                sendText.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryGray))
                sendText.text = context?.let {
                    String.format(
                        Locale.CHINA,
                        it.getString(R.string.code_send_retry_hint),
                        "${millisUntilFinished / 1000}s"
                    )
                } ?: ""
            }

            override fun onFinish() {
                sendText.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                sendText.text = context?.getString(R.string.code_send_retry) ?: ""
                sendText.isEnabled = true
            }
        }.start()
    }

    fun stop() {
        countDownTimer?.cancel()
        sendText.text = context?.getString(R.string.verify_code_req) ?: ""
        sendText.isEnabled = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }
}
