package com.treasure.basic.ui.view

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.treasure.basic.R
import com.treasure.basic.helper.CommonCallback
import com.treasure.basic.utils.KeyboardHelper

class SearchToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var tvTitle: TextView? = null
    private var etSearch: EditText? = null
    private var llSearch: LinearLayout? = null
    private var ivSearch: ImageView? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_search_title_bar, this, true)
        tvTitle = findViewById(R.id.tv_toolbar_title)
        findViewById<View>(R.id.tv_toolbar_cancel).setOnClickListener { v: View? -> toggleSearch() }
        etSearch = findViewById(R.id.et_toolbar_search)
        ivSearch = findViewById(R.id.iv_toolbar_search)
        ivSearch?.setOnClickListener { v: View? -> toggleSearch() }
        llSearch = findViewById(R.id.ll_toolbar_search)
        findViewById<View>(R.id.iv_toolbar_return).setOnClickListener { v: View? ->
            (getContext() as? Activity)?.finish()
        }

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SearchTitleBar)
            try {
                val title = a.getString(R.styleable.SearchTitleBar_titleText)
                val hint = a.getString(R.styleable.SearchTitleBar_searchHint)
                val backDrawable = a.getDrawable(R.styleable.SearchTitleBar_backIcon)
                val searchDrawable = a.getDrawable(R.styleable.SearchTitleBar_searchIcon)
                if (!TextUtils.isEmpty(title)) tvTitle?.text = title
                if (!TextUtils.isEmpty(hint)) etSearch?.hint = hint
                if (backDrawable != null) (findViewById<View>(R.id.iv_toolbar_return) as ImageView).setImageDrawable(
                    backDrawable
                )
                if (searchDrawable != null) ivSearch?.setImageDrawable(searchDrawable)
            } finally {
                a.recycle()
            }
        }

        etSearch?.setOnEditorActionListener(OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = etSearch?.text.toString().trim { it <= ' ' }
                if (cb != null) cb?.onContinue(keyword)
                KeyboardHelper.hideKeyboard(etSearch)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun toggleSearch() {
        if (llSearch?.visibility == GONE) {
            tvTitle?.visibility = GONE
            ivSearch?.visibility = GONE
            llSearch?.visibility = VISIBLE
            etSearch?.requestFocus()
            showKeyboard()
        } else {
            etSearch?.setText("")
            llSearch?.visibility = GONE
            tvTitle?.visibility = VISIBLE
            ivSearch?.visibility = VISIBLE
            KeyboardHelper.hideKeyboard(etSearch)
        }
    }

    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    fun setTitle(title: String?) {
        tvTitle?.text = title
    }

    private var cb: CommonCallback<String>? = null

    init {
        init(context, attrs)
    }

    fun setSearchListener(cb: CommonCallback<String>?) {
        this.cb = cb
    }

    val searchText: String
        get() = etSearch?.text.toString().trim { it <= ' ' }
}

