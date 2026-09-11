package com.treasure.basic.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner

/**
BottomDialogBuilder(this)
.setLayout(R.layout.dialog_bottom_custom_layout)
.setCancelable(true)
.onBindView { view, dialog ->
val confirm = view.findViewById<Button>(R.id.btnConfirm)
val cancel = view.findViewById<Button>(R.id.btnCancel)

confirm.setOnClickListener {
Toast.makeText(this, "已确认", Toast.LENGTH_SHORT).show()
dialog.dismiss()
}

cancel.setOnClickListener {
dialog.dismiss()
}
}
.show()

 */

class BottomDialogBuilder(private val context: Context) {

    private var layoutRes: Int = 0
    private var onBindView: ((View?, MaterialDialog) -> Unit)? = null
    private var cancelable: Boolean = true
    private var dimAmount: Float = 0.5f
    private var dialog: MaterialDialog? = null

    fun setLayout(@LayoutRes layoutRes: Int): BottomDialogBuilder {
        this.layoutRes = layoutRes
        return this
    }

    fun setCancelable(cancelable: Boolean): BottomDialogBuilder {
        this.cancelable = cancelable
        return this
    }

    fun setDimAmount(dimAmount: Float): BottomDialogBuilder {
        this.dimAmount = dimAmount
        return this
    }

    fun onBindView(listener: (View?, MaterialDialog) -> Unit): BottomDialogBuilder {
        this.onBindView = listener
        return this
    }

    fun show(): MaterialDialog? {
        if (layoutRes == 0) throw IllegalArgumentException("LayoutRes must be set!")

        dialog = MaterialDialog(context).apply {
            cancelable(cancelable)
            cancelOnTouchOutside(cancelable)
            lifecycleOwner(context as? LifecycleOwner)
            customView(viewRes = layoutRes, scrollable = false)
        }

        // 设置弹窗样式：底部弹出 + 宽高 + 动画 + 圆角
        dialog?.window?.apply {
            setGravity(Gravity.BOTTOM)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
            attributes = attributes.apply {
                this.dimAmount = this@BottomDialogBuilder.dimAmount
            }
        }

        // View 回调绑定
        val view = dialog?.getCustomView()
        onBindView?.invoke(view, dialog!!)

        dialog?.show()
        return dialog
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}
