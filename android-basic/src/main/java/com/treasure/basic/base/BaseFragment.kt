package com.treasure.basic.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.treasure.basic.helper.LogHelper
import com.treasure.basic.ui.view.FullLoadingView

/**
 * date:2021/6/10
 * author:李超(licha)
 * function:
 */
open class BaseFragment : Fragment() {
    private var loadingView:FullLoadingView?=null
    private var isViewCreated = false
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //设置xml布局
        return inflater.inflate(setLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isViewCreated = true
    }

    protected open fun lazyInit(view: View?) {}
    protected open fun setLayoutId(): Int = 0

    override fun onResume() {
        super.onResume()
        if (isViewCreated && !isDataLoaded && view != null) {
            lazyInit(view)
            LogHelper.d("lazyInit: " + javaClass.simpleName)
            isDataLoaded = true
        }
    }

    open fun showLoading() {
        loadingView?.show() ?: run {
            activity?.let {ctx ->
                loadingView = FullLoadingView(ctx)
                val rootView = ctx.findViewById<ViewGroup>(android.R.id.content)
                rootView.addView(
                    loadingView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                loadingView?.show()
            }
        }
    }

    open fun hindLoading() = loadingView?.hide()

    override fun onDestroyView() {
        super.onDestroyView()
    }
}