package com.treasure.basic.ui.view

/**
 * Created by treasure_ct on 2025/08/15
 * Description:
 */
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import kotlin.math.abs

class HorizontalNestedScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private var lastX = 0f
    private var lastY = 0f
    private var isDraggingHorizontally = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                lastY = ev.y
                isDraggingHorizontally = false
                super.onInterceptTouchEvent(ev) // 保证后续事件正常
                return false // 不立即拦截
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - lastX)
                val dy = abs(ev.y - lastY)

                if (dx > dy && dx > 10) {
                    // 横向滑动，拦截事件
                    isDraggingHorizontally = true
                    return true
                } else if (dy > dx) {
                    // 竖向滑动，不拦截，交给父 RecyclerView
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDraggingHorizontally = false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(ev)
    }
}
