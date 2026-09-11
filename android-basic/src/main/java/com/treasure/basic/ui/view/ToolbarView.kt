package com.treasure.basic.ui.view

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.treasure.basic.R
import com.treasure.basic.utils.DoubleClickUtilsKt.setDebouncedClickListener

class ToolbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val backIcon: ImageView
    private val titleText: TextView
    private val rightIcon: ImageView
    private val tvExport: TextView
    private val btnSave: TextView
    private val btnCancel: TextView
    private val etSearch: EditSearchText

    var todoBack: (() -> Unit)? = null
    var todoMore: (() -> Unit)? = null
    var todoExport: ((Boolean) -> Unit)? = null
    var todoSave: (() -> Unit)? = null
    var todoCancel: (() -> Unit)? = null
    var todoSearch: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_toolbar, this, true)
        backIcon = findViewById(R.id.iv_back)
        titleText = findViewById(R.id.tv_toolbar_title)
        rightIcon = findViewById(R.id.iv_right)
        tvExport = findViewById(R.id.tv_export)
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)
        etSearch = findViewById(R.id.et_toolbar_search)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarView)
        titleText.text = typedArray.getString(R.styleable.ToolbarView_tb_title)
        val showBack = typedArray.getBoolean(R.styleable.ToolbarView_tb_showBack, true)
        backIcon.visibility = if (showBack) View.VISIBLE else View.GONE
        val showExport = typedArray.getBoolean(R.styleable.ToolbarView_tb_showExport, false)
        tvExport.visibility = if (showExport) View.VISIBLE else View.GONE
        val showSave = typedArray.getBoolean(R.styleable.ToolbarView_tb_showSave, false)
        btnSave.visibility = if (showSave) View.VISIBLE else View.GONE
        val showCancel = typedArray.getBoolean(R.styleable.ToolbarView_tb_showCancel, false)
        btnCancel.visibility = if (showCancel) View.VISIBLE else View.GONE

        val rightIconRes = typedArray.getResourceId(R.styleable.ToolbarView_tb_rightIcon, 0)
        if (rightIconRes != 0) {
            rightIcon.setImageResource(rightIconRes)
            rightIcon.visibility = View.VISIBLE
        } else {
            rightIcon.visibility = View.GONE
        }

        val showSearch = typedArray.getBoolean(R.styleable.ToolbarView_tb_showSearch, false)
        etSearch.visibility = if (showSearch) View.VISIBLE else View.GONE
        etSearch.setHint(typedArray.getString(R.styleable.ToolbarView_tb_searchHint) ?: "")

        typedArray.recycle()

        backIcon.setDebouncedClickListener {
            todoBack?.invoke() ?: (context as? Activity)?.finish()
        }
        rightIcon.setDebouncedClickListener { todoMore?.invoke() }
        tvExport.setDebouncedClickListener {
            if (TextUtils.equals(tvExport.text, context.getString(R.string.export_cancel))) {
                tvExport.text = context.getString(R.string.export)
                tvExport.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_export, 0, 0, 0
                )
                todoExport?.invoke(false)
            } else {
                tvExport.text = context.getString(R.string.export_cancel)
                tvExport.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                todoExport?.invoke(true)
            }

        }
        btnSave.setDebouncedClickListener { todoSave?.invoke() }
        btnCancel.setDebouncedClickListener { todoCancel?.invoke() }
        etSearch.setOnSearchAction { todoSearch?.invoke(etSearch.getText()) }
    }

    fun setTitle(text: String) {
        titleText.text = text
    }

    fun getSearchText(): String {
        return etSearch.getText()
    }

    fun setRightIconVisible(visible: Boolean) {
        rightIcon.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setCancelVisible(visible: Boolean) {
        btnCancel.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setSearchVisible(visible: Boolean, hint: String = "") {
        etSearch.visibility = if (visible) View.VISIBLE else View.GONE
        etSearch.setHint(hint)
    }

    fun setExportIconVisible(visible: Boolean) {
        tvExport.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun resetExport() {
        tvExport.text = context.getString(R.string.export)
        tvExport.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_export, 0, 0, 0
        )
    }

    fun setOnBackClickListener(action: () -> Unit) {
        todoBack = action
    }

    fun setOnRightClickListener(action: () -> Unit) {
        todoMore = action
    }

    fun actionExportClickListener(action: (Boolean) -> Unit) {
        todoExport = action
    }

    fun actionSaveListener(action: () -> Unit) {
        todoSave = action
    }

    fun actionCancelListener(action: () -> Unit) {
        todoCancel = action
    }

    fun actionSearchListener(action: ((String) -> Unit)?) {
        todoSearch = action
    }

    // 为 btnSave 添加 getter 方法
    fun getSaveButton(): TextView {
        return btnSave
    }
}
