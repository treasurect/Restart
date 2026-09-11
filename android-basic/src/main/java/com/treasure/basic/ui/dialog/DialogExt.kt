package com.treasure.basic.ui.dialog

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.treasure.basic.R

/**
 * Created by treasure_ct on 2025/08/20
 * Description:
 */
object DialogExt {
    /**
     * 通用双按钮Dialog
     * @param context 上下文
     * @param title 标题（可选，默认为"提示"）
     * @param content 内容
     * @param leftBtnText 左侧按钮文字（可选，默认为"确定"）
     * @param leftBtnColor 左侧按钮颜色（可选，默认为主题色）
     * @param rightBtnText 右侧按钮文字（可选，默认为"取消"）
     * @param rightBtnColor 右侧按钮颜色（可选，默认为灰色）
     * @param leftCallback 左侧按钮点击回调
     * @param rightCallback 右侧按钮点击回调
     */
    fun showCommonDialog(
        context: Context?,
        content: String,
        title: String = context?.getString(R.string.hint) ?: "提示",
        leftBtnText: String = context?.getString(com.treasure.basic.R.string.confirm) ?: "确定",
        leftBtnColor: Int = R.color.colorPrimary,
        rightBtnText: String = context?.getString(com.treasure.basic.R.string.cancel) ?: "取消",
        rightBtnColor: Int = R.color.colorPrimary,
        leftCallback: (() -> Unit)? = null,
        rightCallback: (() -> Unit)? = null
    ) {
        if (context == null) return

        CenterDialogBuilder(context)
            .setLayout(R.layout.dialog_common_two_btn)
            .setCancelable(true)
            .onBindView { view, dialog ->
                // 设置标题
                view?.findViewById<TextView>(R.id.tvTitle)?.text = title

                // 设置内容
                view?.findViewById<TextView>(R.id.tvDesc)?.text = content

                // 设置左侧按钮文字和颜色
                view?.findViewById<TextView>(R.id.btnLeft)?.apply {
                    text = leftBtnText
                    setTextColor(ContextCompat.getColor(context, leftBtnColor))
                }

                // 设置右侧按钮文字和颜色
                view?.findViewById<TextView>(R.id.btnRight)?.apply {
                    text = rightBtnText
                    setTextColor(ContextCompat.getColor(context, rightBtnColor))
                }

                // 设置左侧按钮点击事件
                view?.findViewById<TextView>(R.id.btnLeft)?.setOnClickListener {
                    leftCallback?.invoke()
                    dialog.dismiss()
                }

                // 设置右侧按钮点击事件
                view?.findViewById<TextView>(R.id.btnRight)?.setOnClickListener {
                    rightCallback?.invoke()
                    dialog.dismiss()
                }
            }.show()
    }

    /**
     * 通用单按钮Dialog
     * @param context 上下文
     * @param title 标题（可选，默认为"提示"）
     * @param content 内容
     * @param BtnText 按钮文字（可选，默认为"确定"）
     * @param BtnColor 按钮颜色（可选，默认为主题色）
     * @param Callback 按钮点击回调
     */
    fun showSingleCommonDialog(
        context: Context?,
        content: String,
        title: String = context?.getString(com.treasure.basic.R.string.hint) ?: "提示",
        BtnText: String = context?.getString(com.treasure.basic.R.string.confirm) ?: "确定",
        BtnColor: Int = R.color.colorPrimary,
        Callback: (() -> Unit)? = null,
    ) {
        if (context == null) return

        CenterDialogBuilder(context)
            .setLayout(R.layout.dialog_common_single_btn)
            .setCancelable(true)
            .onBindView { view, dialog ->
                // 设置标题
                view?.findViewById<TextView>(R.id.tvTitle)?.text = title

                // 设置内容
                view?.findViewById<TextView>(R.id.tvDesc)?.text = content

                // 设置左侧按钮文字和颜色
                view?.findViewById<TextView>(R.id.btn)?.apply {
                    text = BtnText
                    setTextColor(ContextCompat.getColor(context, BtnColor))
                }

                // 设置左侧按钮点击事件
                view?.findViewById<TextView>(R.id.btn)?.setOnClickListener {
                    Callback?.invoke()
                    dialog.dismiss()
                }
            }.show()
    }
}