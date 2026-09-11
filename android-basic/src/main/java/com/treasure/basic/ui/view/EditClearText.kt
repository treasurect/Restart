package com.treasure.basic.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.treasure.basic.R

class EditClearText : AppCompatEditText, OnTouchListener, OnFocusChangeListener, TextWatcher {
    private var clearIcon: Drawable? = null
    private var onFocusChangeListener: OnFocusChangeListener? = null
    private var onTouchListener: OnTouchListener? = null
    private var isClearIconAlwaysVisible = false
    private var clearIconPadding = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // 获取自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)
        try {
            clearIcon = typedArray.getDrawable(R.styleable.ClearEditText_clearIcon)
            clearIconPadding =
                typedArray.getDimensionPixelSize(R.styleable.ClearEditText_clearIconPadding, 0)
        } finally {
            typedArray.recycle()
        }

        // 如果没有设置清除图标，使用默认图标
        if (clearIcon == null) {
            clearIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_close, null)
        }

        // 设置图标边界
        setClearIconBounds()

        background = null
        textSize = 14f

        // 默认隐藏清除图标
        setClearIconVisible(false)

        // 设置监听器
        super.setOnFocusChangeListener(this)
        super.setOnTouchListener(this)
        super.addTextChangedListener(this)
    }

    private fun setClearIconBounds() {
        if (clearIcon != null) {
            clearIcon!!.setBounds(
                -clearIconPadding,
                -clearIconPadding,
                clearIcon!!.intrinsicWidth + clearIconPadding,
                clearIcon!!.intrinsicHeight + clearIconPadding
            )
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        val compoundDrawables = compoundDrawables
        val endDrawable = if (visible && !text.toString().isEmpty()) clearIcon else null

        setCompoundDrawables(
            compoundDrawables[0],  // left
            compoundDrawables[1],  // top
            endDrawable,  // right
            compoundDrawables[3] // bottom
        )
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        this.onFocusChangeListener = l
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        this.onTouchListener = l
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus && !isClearIconAlwaysVisible) {
            setClearIconVisible(!text.toString().isEmpty())
        } else {
            setClearIconVisible(isClearIconAlwaysVisible && !text.toString().isEmpty())
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener!!.onFocusChange(v, hasFocus)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val tappedClearIcon = event.x > (width - paddingRight - clearIcon!!.intrinsicWidth)
            if (tappedClearIcon) {
                if (event.action == MotionEvent.ACTION_UP) {
                    setText("")
                }
                return true
            }
        }

        if (onTouchListener != null) {
            return onTouchListener!!.onTouch(v, event)
        }

        return false
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // 不需要实现
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused || isClearIconAlwaysVisible) {
            setClearIconVisible(!s.toString().isEmpty())
        }
    }

    override fun afterTextChanged(s: Editable) {
        // 不需要实现
    }

    // 公开方法
    fun setClearIconAlwaysVisible(visible: Boolean) {
        this.isClearIconAlwaysVisible = visible
        setClearIconVisible(visible && !text.toString().isEmpty())
    }

    fun setClearIcon(drawable: Drawable?) {
        this.clearIcon = drawable
        setClearIconBounds()
        setClearIconVisible(!text.toString().isEmpty())
    }

    fun setClearIconPadding(padding: Int) {
        this.clearIconPadding = padding
        setClearIconBounds()
        setClearIconVisible(!text.toString().isEmpty())
    }
}
