package com.treasure.basic.ui.dialog

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.treasure.basic.R
import com.treasure.basic.utils.ScreenUtils

/**
CenterDialogBuilder(this)
.setLayout(R.layout.dialog_center_custom)
.setCancelable(true)
.setWidthPercent(0.75f)
.onBindView { view, dialog ->
view.findViewById<Button>(R.id.btnOK).setOnClickListener {
Toast.makeText(this, "已点击确认", Toast.LENGTH_SHORT).show()
dialog.dismiss()
}
view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
dialog.dismiss()
}
}
.show()

 */
class CenterDialogBuilder(private val context: Context?) {

    private var layoutRes: Int = 0
    private var cancelable: Boolean = true
    private var dimAmount: Float = 0.5f
    private var widthPercent: Float = 0.68f
    private var bgRes: Int = R.drawable.bg_solid_white_r_8
    private var onBindView: ((View?, MaterialDialog) -> Unit)? = null
    private var dialog: MaterialDialog? = null

    fun setLayout(@LayoutRes layoutRes: Int): CenterDialogBuilder {
        this.layoutRes = layoutRes
        return this
    }

    fun setCancelable(cancelable: Boolean): CenterDialogBuilder {
        this.cancelable = cancelable
        return this
    }

    fun setDimAmount(dim: Float): CenterDialogBuilder {
        this.dimAmount = dim
        return this
    }

    fun setWidthPercent(percent: Float): CenterDialogBuilder {
        this.widthPercent = percent.coerceIn(0.3f, 1f)
        return this
    }

    fun setBackground(res:Int): CenterDialogBuilder {
        this.bgRes = res
        return this
    }

    fun onBindView(bind: (View?, MaterialDialog) -> Unit): CenterDialogBuilder {
        this.onBindView = bind
        return this
    }

    fun show(hasLifecycleOwner: Boolean = true): MaterialDialog? {
        if (context == null) return null
        if (layoutRes == 0) throw IllegalArgumentException("LayoutRes must be set!")
        dialog = MaterialDialog(this@CenterDialogBuilder.context).apply {
            cancelable(cancelable)
            cancelOnTouchOutside(cancelable)
            if (hasLifecycleOwner) {
                lifecycleOwner(this@CenterDialogBuilder.context as? LifecycleOwner)
            }
            customView(viewRes = layoutRes, scrollable = false, noVerticalPadding = true)
        }

        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            setBackgroundDrawableResource(android.R.color.transparent)
            attributes = attributes.apply {
                dimAmount = this@CenterDialogBuilder.dimAmount
            }
        }
        dialog?.findViewById<View>(com.afollestad.materialdialogs.R.id.md_root)?.apply {
            background = if (bgRes == 0) null else ContextCompat.getDrawable(context, bgRes)
            setPadding(0, 0, 0, 0)
        }

        onBindView?.invoke(dialog?.getCustomView(), dialog!!)
        dialog?.show()
        dialog?.window?.setLayout(
            (ScreenUtils.getScreenWidth(context) * widthPercent).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }


    fun dismiss() {
        dialog?.dismiss()
    }
}
