package com.treasure.basic.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.treasure.basic.utils.SystemBarUtil

class StatusBarSpaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val params = layoutParams
        params?.height = SystemBarUtil.getStatusBarHeight(context)
        layoutParams = params
    }

}
