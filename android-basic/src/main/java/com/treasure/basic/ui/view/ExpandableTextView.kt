package com.treasure.basic.ui.view

/**
 * Created by treasure_ct on 2025/08/05
 * Description:
 */
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var isExpanded = false // 当前是否展开
    private var originalText: CharSequence? = null

    init {
        // 默认只显示一行
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END

        // 点击切换
//        setOnClickListener {
//            toggle()
//        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text
        super.setText(text, type)
    }

     fun toggle() {
        isExpanded = !isExpanded
        if (isExpanded) {
            // 展开
            maxLines = Int.MAX_VALUE
            ellipsize = null
        } else {
            // 收起
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
    }
}
