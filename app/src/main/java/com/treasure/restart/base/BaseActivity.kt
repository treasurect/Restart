package com.treasure.restart.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.treasure.basic.helper.ActivityCollector
import com.treasure.basic.utils.PermissionHelper
import com.treasure.basic.utils.SystemBarUtil
import com.treasure.basic.utils.SystemBarUtil.enableBottomStatusBar
import com.treasure.basic.utils.SystemBarUtil.enableImmersiveStatusBar
import com.treasure.basic.view.LoadingView


/**
 * date:2021/5/25
 * author:李超(licha)
 */
open class BaseActivity : AppCompatActivity() {
    //private var loadingView: FullLoadingView? = null
    var placeHolderView: View? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        setStatusBar()
        ActivityCollector.addActivity(this)
        lazyInit()
        (window?.decorView as? ViewGroup)?.let {
            placeHolderView = layoutInflater.inflate(com.treasure.basic.R.layout.include_empty_view, it, false)
        }
    }

    private fun setStatusBar() {
        if (immersiveStatusBarEnable) {
            this.enableImmersiveStatusBar(true)
        } else {
            this.enableBottomStatusBar(true)
        }
    }


    protected open fun setContentView() {}

    protected open fun lazyInit() {}

    protected open var immersiveStatusBarEnable = false

    open fun showLoading() {
//        loadingView?.show() ?: run {
//            loadingView = FullLoadingView(this@BaseActivity)
//            val rootView = findViewById<ViewGroup>(R.id.content)
//            rootView.addView(
//                loadingView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            loadingView?.show()
//        }
    }

    open fun hindLoading() {
//         loadingView?.hide()
    }

    protected fun requestPermission(permissions: Array<String>) {
        PermissionHelper.requestPermissions(
            this,
            permissions,
            onGranted = { onPermissionGranted(permissions) })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(
            this,
            permissions,
            grantResults,
            onGranted = { onPermissionGranted(permissions) })
    }

    protected open fun onPermissionGranted(permissions: Array<String>) {}

    fun intervalTop(view: View) {
        val layoutParams = view.layoutParams
        layoutParams.height += SystemBarUtil.getStatusBarHeight(this)
        view.layoutParams = layoutParams
    }

    fun isActive(): Boolean {
        return this.isDestroyed.not() && this.isFinishing.not()
    }

    private var loadingView: LoadingView? = null
    protected fun showLoading(message: String = "加载中...") {
        loadingView?.show(message) ?: run {
            window.decorView.post {
                val decorView = window.decorView as ViewGroup
                if (loadingView != null) return@post
                loadingView = LoadingView(this)
                decorView.addView(loadingView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }
    }

    protected fun hideLoading() {
        loadingView?.hide()
    }

    override fun finish() {
        if (this.isFinishing.not() && this.isDestroyed.not()) {
            super.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
        //EventBus.getDefault().unregister(this)
    }
}
