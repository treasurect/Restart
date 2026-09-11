package com.treasure.basic.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.treasure.basic.R
import java.io.File

object GlideUtils {

    /** 基础 RequestOptions：缓存所有、无动画 */
    private fun getDefaultOptions(): RequestOptions {
        return RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
    }

    /** 加载普通图 */
    fun loadImage(context: Context, source: Any?, imageView: ImageView) {
        val glide = getRequestManager(context)
        val request = glide
            .load(resolveSource(source))
            .apply(getDefaultOptions())

        request.into(imageView)
    }


    /** 加载圆角图（默认 CenterCrop + RoundedCorner） */
    fun loadRoundCorner(
        context: Context, source: Any?, imageView: ImageView,
        cornerRadius: Int, errorRes: Int = R.drawable.ic_default_icon
    ) {
        val options = getDefaultOptions()
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
        getRequestManager(context)
            .load(resolveSource(source))
            .apply(options)
            .error(errorRes)
            .into(imageView)
    }

    fun loadRoundCorner2(
        context: Context, source: Any?, imageView: ImageView,
        cornerRadius: Int, errorRes: Int = R.drawable.ic_default_icon
    ) {
        val options = getDefaultOptions()
            .transform(CenterInside(), RoundedCorners(cornerRadius))
        getRequestManager(context)
            .load(resolveSource(source))
            .apply(options)
            .error(errorRes)
            .into(imageView)
    }

    /** 加载圆形图（用于头像） */
    fun loadCircle(context: Context, source: Any?, imageView: ImageView) {
        val options = getDefaultOptions()
            .transform(CircleCrop())
        getRequestManager(context)
            .load(resolveSource(source))
            .apply(options)
            .into(imageView)
    }

    /** 带占位图/错误图 */
    fun loadWithPlace(
        context: Context, source: Any?, imageView: ImageView,
        @DrawableRes placeholderRes: Int, @DrawableRes errorRes: Int? = null, cornerRadius: Int = 0
    ) {
        val options = getDefaultOptions()
            .placeholder(placeholderRes)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
        errorRes?.let { options.error(errorRes) }

        getRequestManager(context)
            .load(resolveSource(source))
            .apply(options)
            .into(imageView)
    }

    /** 带加载监听 */
    fun loadWithListener(
        context: Context,
        url: String?, imageView: ImageView, listener: RequestListener<Drawable>
    ) {
        getRequestManager(context)
            .load(url)
            .listener(listener)
            .apply(getDefaultOptions())
            .into(imageView)
    }

    /** 异步加载 Bitmap（避免 UI 线程操作） */
    fun loadBitmap(context: Context, url: String?, callback: (Bitmap?) -> Unit) {
        getRequestManager(context)
            .asBitmap()
            .load(url)
            .apply(getDefaultOptions())
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 可用于回收占位图
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    callback(null)
                }
            })
    }

    private fun resolveSource(source: Any?): Any? {
        return when (source) {
            is String -> {
                if (source.startsWith("http", true)) {
                    source
                } else {
                    // 可能是本地路径，尝试转为 File
                    File(source)
                }
            }

            is Int -> source  // R.drawable.xxx
            is File -> source
            else -> null
        }
    }


    /** 下载图片文件（放子线程里用） */
    fun downloadImageFile(context: Context, url: String?): File? {
        return try {
            val future = getRequestManager(context).downloadOnly().load(url).submit()
            future.get() // 阻塞获取，推荐放子线程
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** 自动根据 Fragment/Activity/Context 获取合适的 RequestManager */
    private fun getRequestManager(context: Any): RequestManager {
        return when (context) {
            is Context -> Glide.with(context)
            is Fragment -> Glide.with(context)
            is Activity -> Glide.with(context)
            else -> throw IllegalArgumentException("不支持的 Context 类型")
        }
    }
}
