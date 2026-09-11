package com.treasure.basic.utils
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.treasure.basic.R

object ToastUtils {

    private var toast: Toast? = null
    private var appContext: Context? = null

    fun init(app: Context) {
        appContext = app.applicationContext
    }

    fun show(message: String) {
        show(message, false)
    }

    fun show(message: String, isLong: Boolean = false) {
        appContext?.let { context ->
            Handler(Looper.getMainLooper()).post {
                toast?.cancel()
                toast = Toast.makeText(context, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

    fun showCenter(message: String, isLong: Boolean = false) {
        appContext?.let { context ->
            Handler(Looper.getMainLooper()).post {
                toast?.cancel()
                toast = Toast.makeText(context, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.show()
            }
        }
    }

    fun showCustom(message: String) {
        appContext?.let { context ->
            Handler(Looper.getMainLooper()).post {
                toast?.cancel()
                val inflater = LayoutInflater.from(context)
                val layout: View = inflater.inflate(R.layout.toast_custom, null)
                val textView: TextView = layout.findViewById(R.id.tv_message)
                textView.text = message

                toast = Toast(context)
                toast?.duration = Toast.LENGTH_SHORT
                toast?.view = layout
                toast?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
                toast?.show()
            }
        }
    }

}
