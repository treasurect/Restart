package com.treasure.basic.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.treasure.basic.R

class BottomSheetDialogBuilder(private val context: Context) {

    private var layoutRes: Int = 0
    private var cancelable: Boolean = true
    private var onBindView: ((View, BottomSheetDialog) -> Unit)? = null
    private var dialog: BottomSheetDialog? = null

    fun setLayout(@LayoutRes layoutRes: Int): BottomSheetDialogBuilder {
        this.layoutRes = layoutRes
        return this
    }

    fun setCancelable(cancelable: Boolean): BottomSheetDialogBuilder {
        this.cancelable = cancelable
        return this
    }

    fun onBindView(bind: (View, BottomSheetDialog) -> Unit): BottomSheetDialogBuilder {
        this.onBindView = bind
        return this
    }

    fun show(): BottomSheetDialog {
        val sheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(layoutRes, null)
        sheetDialog.setContentView(view)
        sheetDialog.setCancelable(cancelable)
        sheetDialog.setCanceledOnTouchOutside(cancelable)

        // 可设置圆角背景（需配合样式或 layout 设置）
//        view.background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded_top)

        // View 回调
        onBindView?.invoke(view, sheetDialog)

        sheetDialog.show()
        dialog = sheetDialog
        (dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout)?.let {
            it.background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded_top)
            it.setPadding(0, 0, 0, 0)
        }
        return sheetDialog
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}
