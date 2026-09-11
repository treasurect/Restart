package com.treasure.basic.utils
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.treasure.basic.utils.ToastUtils.show

object KeyboardHelper {

    /**
     * 显示软键盘
     * @param view 当前拥有焦点的 View，通常是 EditText
     */
    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 隐藏软键盘
     * @param view 当前的 View（建议传入 EditText 或当前焦点 View）
     */
    fun hideKeyboard(view: View?) {
        view ?: return
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏软键盘（传入 Activity）
     */
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        hideKeyboard(view)
    }

    /**
     * 切换软键盘（如果显示则隐藏，隐藏则显示）
     */
    fun toggleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 判断软键盘是否正在显示（可能不准确，仅供参考）
     */
    fun isKeyboardActive(context: Context): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        return imm?.isActive ?: false
    }

    fun copy(context: Context, value: String?) {
        // 获取剪贴板管理器
        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        // 创建一个剪贴板数据对象
        val clip = ClipData.newPlainText("label", value)

        // 将剪贴板数据设置为创建的ClipData对象
        clipboard.setPrimaryClip(clip)
        show("已复制")
    }
}
