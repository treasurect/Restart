package com.treasure.basic.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.sign

class NestedScrollableHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var initialX = 0f
    private var initialY = 0f

    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View

            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }

            return v as? ViewPager2
        }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        handleInterceptTouchEvent(ev)

        return super.onInterceptTouchEvent(ev)
    }

    private fun handleInterceptTouchEvent(ev: MotionEvent) {

        val parentViewPager = parentViewPager ?: return

        val orientation = parentViewPager.orientation

        // child
        val child = getChildAt(0) ?: return

        when (ev.action) {

            MotionEvent.ACTION_DOWN -> {

                initialX = ev.x
                initialY = ev.y

                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {

                val dx = ev.x - initialX
                val dy = ev.y - initialY

                val isHorizontal =
                    orientation == ViewPager2.ORIENTATION_HORIZONTAL

                val scaledDx =
                    abs(dx) * if (isHorizontal) 0.5f else 1f

                val scaledDy =
                    abs(dy) * if (isHorizontal) 1f else 0.5f

                // 是同方向滑动
                if (scaledDx > scaledDy) {

                    val direction =
                        if (isHorizontal) dx else dy

                    // 关键逻辑
                    if (canChildScroll(
                            child,
                            orientation,
                            direction
                        )
                    ) {

                        // child还能滑
                        parent.requestDisallowInterceptTouchEvent(
                            true
                        )

                    } else {

                        // child到边界
                        // 父ViewPager接管
                        parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }

                } else {

                    // 垂直方向
                    parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
            }
        }
    }

    /**
     * child是否还能继续滑动
     */
    private fun canChildScroll(
        child: View,
        orientation: Int,
        delta: Float
    ): Boolean {

        val direction = -delta.sign.toInt()

        return when (orientation) {

            ViewPager2.ORIENTATION_HORIZONTAL -> {
                child.canScrollHorizontally(direction)
            }

            else -> {
                child.canScrollVertically(direction)
            }
        }
    }
}