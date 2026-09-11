package com.treasure.basic.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.treasure.basic.R
import com.treasure.basic.utils.DoubleClickUtilsKt.setDebouncedClickListener
import com.treasure.basic.utils.ScreenUtils

class EditSearchText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val editText: EditText
    private val searchIcon: ImageView
    private val btnCancel: TextView

    private var todoCancel: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_search_edit_text, this, true)

        editText = findViewById(R.id.etSearch)
        searchIcon = findViewById(R.id.ivSearchIcon)
        btnCancel = findViewById(R.id.tv_cancel_1)

        context.withStyledAttributes(attrs, R.styleable.SearchEditText) {
            editText.hint = getString(R.styleable.SearchEditText_edit_hint)
            searchIcon.visibility =
                if (getBoolean(R.styleable.SearchEditText_icon_visibility, true)) VISIBLE else GONE
            btnCancel.visibility =
                if (getBoolean(R.styleable.SearchEditText_cancel_visible, false)) VISIBLE else GONE
        }
        background = ContextCompat.getDrawable(context, R.drawable.bg_solid_f7f8fa_r_8)
        setPadding(ScreenUtils.dp2px(context, 8f), 0, ScreenUtils.dp2px(context, 8f), 0)
        gravity = Gravity.CENTER_VERTICAL

        btnCancel.setDebouncedClickListener {
            todoCancel?.invoke()
        }
    }

    fun getEditText(): EditText = editText

    fun getText(): String = editText.text.toString().trim()
    fun setText(text:String) {
        editText.setText(text)
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setIcon(resId: Int) {
        searchIcon.setImageResource(resId)
    }

    fun setOnSearchAction(callback: (String) -> Unit) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                callback(getText())
                true
            } else false
        }
    }

    fun setOnTextChanged(callback: (String,Int) -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                callback.invoke(s?.toString() ?: "", 0)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                callback.invoke(s?.toString() ?: "", 1)
            }

            override fun afterTextChanged(s: Editable?) {
                callback.invoke(s?.toString() ?: "", 2)
            }

        })
    }

    fun setActionCancel(action:()->Unit){
        todoCancel = action
    }
}
