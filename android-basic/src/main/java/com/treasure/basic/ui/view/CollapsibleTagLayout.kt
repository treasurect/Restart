package com.treasure.basic.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.treasure.basic.ContextHolder
import com.treasure.basic.R
import com.treasure.basic.utils.ScreenUtils

class CollapsibleTagLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val flexboxLayout = FlexboxLayout(context)
    private val toggleView = ImageView(context)
    var isExpanded = false
    var allTags: List<String> = emptyList()

    private var maxVisibleTags = 3
    private var dePageType = 0// 1 资源-园区  2 招商列表-产业标签 4 链式招商-企业标签 3 其他招商-企业标签 5 资源-企业
    private var tagPadding = ScreenUtils.dp2px(ContextHolder.app(), 4f)
    private var tagTextSize = 11f
    private var tagBackgrounds = arrayOf(
        R.drawable.bg_list_tag_1,
        R.drawable.bg_list_tag_2,
        R.drawable.bg_list_tag_3,
        R.drawable.bg_list_tag_4
    )
    private var tagTextColors = arrayOf(
        Color.parseColor("#6699CC"),
        Color.parseColor("#AA71E3"),
        Color.parseColor("#BA825D"),
        Color.parseColor("#A68D53")
    )
    private var tagDefaultBackground1 = R.drawable.bg_list_tag_default
    private var tagDefaultBackground2 = R.drawable.bg_list_tag_default_2
    private var tagDefaultTextColor1 = Color.parseColor("#b31152FF")
    private var tagDefaultTextColor2 = Color.parseColor("#A2A7BC")
    var offsetExpandedListener: ((Boolean) -> Unit)? = null

    init {
        orientation = HORIZONTAL

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleTagLayout)
        dePageType = ta.getInt(R.styleable.CollapsibleTagLayout_default_bg_type, 0)
        ta.recycle()

        initFlexbox()
        if (dePageType in arrayOf(1, 5)) initToggle()
    }

    private fun initFlexbox() {
        flexboxLayout.apply {
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            justifyContent = JustifyContent.FLEX_START
        }
        addView(flexboxLayout)
    }

    private fun initToggle() {
        toggleView.apply {
            setImageResource(if (isExpanded) R.drawable.ic_top_arrow_gray else R.drawable.ic_bottom_arrow_gray)
            visibility = GONE
            layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    topMargin = 3.dp
                    leftMargin = 3.dp
                }
        }
        this.setOnClickListener {
            toggle()
        }
        addView(toggleView)
        toggleView.visibility = GONE
    }

    fun setDePageType(dePageType: Int) {
        this.dePageType = dePageType
    }

    fun toggle() {
        isExpanded = !isExpanded
        flexboxLayout.flexWrap = if (isExpanded) FlexWrap.WRAP else FlexWrap.NOWRAP
        if (toggleView.isVisible) {
            toggleView.setImageResource(if (isExpanded) R.drawable.ic_top_arrow_gray else R.drawable.ic_bottom_arrow_gray)
        }
        flexboxLayout.requestLayout()
    }

    fun setTags(tags: List<String>) {
        this.allTags = tags
        updateTags()
        autoWrapByChildren()
    }

    private fun updateTags() {
        flexboxLayout.removeAllViews()
        this.allTags.forEachIndexed { index, tag ->
            val tagView = TextView(context).apply {
                text = tag
                textSize = tagTextSize
                setTextColor(
                    when {
                        dePageType == 1 -> tagDefaultTextColor1
                        dePageType == 2 -> tagDefaultTextColor2
                        index == 0 && dePageType == 3 -> Color.parseColor("#849E4F")
                        index == 0 && dePageType == 4 -> Color.parseColor("#FB3C04")
                        index == 0 && dePageType == 5 -> Color.parseColor("#849E4F")
                        else -> tagTextColors[(index - 1 + tagTextColors.size) % tagTextColors.size]
                    }
                )
                setPadding(tagPadding, tagPadding / 2, tagPadding, tagPadding / 2)
                background = when {
                    dePageType == 1 -> ContextCompat.getDrawable(context, tagDefaultBackground1)
                    dePageType == 2 -> ContextCompat.getDrawable(context, tagDefaultBackground2)
                    index == 0 && dePageType == 3 -> ContextCompat.getDrawable(
                        context, R.drawable.btn_stroke_849e4f_r_2
                    )

                    index == 0 && dePageType == 4 -> ContextCompat.getDrawable(
                        context, R.drawable.btn_stroke_fb3c04_r_2
                    )

                    index == 0 && dePageType == 5 -> ContextCompat.getDrawable(
                        context, R.drawable.btn_stroke_849e4f_r_2
                    )

                    else -> ContextCompat.getDrawable(
                        context,
                        tagBackgrounds[(index - 1 + tagBackgrounds.size) % tagBackgrounds.size]
                    )
                }
                val lp = FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 18.dp)
                lp.flexShrink = 0f
                lp.setMargins(3.dp, 2.dp, 3.dp, 2.dp)
                layoutParams = lp
            }
            flexboxLayout.addView(tagView)
        }
    }

    // 可选：对外暴露自定义属性方法
    fun setMaxVisible(count: Int) {
        this.maxVisibleTags = count
    }

    fun setTagStyle(textSizeSp: Float, textColor: Int, backgroundRes: Int) {
        tagTextSize = textSizeSp
//        tagTextColor = textColor
//        tagBackground = backgroundRes
    }

    // 扩展：单位 dp 转 px
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    interface OnCollapsibleChangeListener {
        fun onChange(isExpanded: Boolean)
    }

    fun autoWrapByChildren() {
        post {
            val totalWidth = (0 until flexboxLayout.childCount).sumOf { i ->
                val child = flexboxLayout.getChildAt(i)
                child.measuredWidth + (child.layoutParams as MarginLayoutParams).let { it.leftMargin + it.rightMargin }
            }
            val offset = totalWidth > width
            flexboxLayout.flexWrap = if (offset) FlexWrap.NOWRAP else FlexWrap.WRAP
            offsetExpandedListener?.invoke(offset)
            switchToggleIcon(offset)
            requestLayout()
        }
    }

    private fun switchToggleIcon(offset: Boolean) {
        if (dePageType !in arrayOf(1, 5)) return
        if (offset) {
            toggleView.visibility = VISIBLE
            toggleView.setImageResource(if (isExpanded) R.drawable.ic_top_arrow_gray else R.drawable.ic_bottom_arrow_gray)
        } else {
            toggleView.visibility = GONE
        }
    }
}
