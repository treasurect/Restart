package com.treasure.basic.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.sqrt


object BitmapUtils {

    /** 将 View 转为 Bitmap */
    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun viewToBitmap200(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, ScreenUtils.dp2px(view.context, 250f), ScreenUtils.dp2px(view.context, 200f))
        view.buildDrawingCache()
        val bitmap = view.drawingCache
        return bitmap
    }

    /** 缩放 Bitmap 到指定宽高 */
    fun scaleBitmap(src: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(src, newWidth, newHeight, true)
    }

    /** Bitmap 转 Base64 字符串 */
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
    }

    /** Base64 字符串转 Bitmap */
    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    /** 保存 Bitmap 到文件 */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    /** 压缩 Bitmap（质量压缩） */
    fun compressBitmap(bitmap: Bitmap, maxSizeKB: Int = 500): Bitmap {
        val stream = ByteArrayOutputStream()
        var quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        while (stream.toByteArray().size / 1024 > maxSizeKB && quality > 10) {
            stream.reset()
            quality -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        }
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /** 获取图片旋转角度 */
    fun getImageRotation(path: String): Int {
        return try {
            val exif = ExifInterface(path)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    /** 旋转 Bitmap */
    fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) return drawable.bitmap
        val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
        val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun resToBitmap(context: Context, @DrawableRes resId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: throw IllegalArgumentException("Resource ID not found")
        return BitmapUtils.drawableToBitmap(drawable)
    }

    /** Bitmap 转 byte[]  */
    fun bitmapToByteArray(bitmap: Bitmap, format: CompressFormat?, quality: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        return outputStream.toByteArray()
    }

    /** 默认使用 PNG（无损）格式  */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        return bitmapToByteArray(bitmap, CompressFormat.PNG, 100)
    }

    /** byte[] 转 Bitmap  */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun byteArrayToBitmap(bytes: ByteArray, width: Int, height: Int): Bitmap? {
        val image = YuvImage(bytes, ImageFormat.NV21, width, height, null)
        val os = ByteArrayOutputStream(bytes.size)
        if (!image.compressToJpeg(Rect(0, 0, width, height), 100, os)) {
            return null
        }
        val tmp = os.toByteArray()
        val bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.size)
        return bmp
    }

    fun downloadImage(url: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var stream: InputStream? = null
        val bmOptions = BitmapFactory.Options()
        bmOptions.inSampleSize = 1
        try {
            stream = getHttpConnection(url)
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions)
            stream!!.close()
        } catch (e1: IOException) {
            e1.printStackTrace()
            println("downloadImage$e1")
        }
        return bitmap
    }

    @Throws(IOException::class)
    fun getHttpConnection(urlString: String?): InputStream? {
        var stream: InputStream? = null
        val url = URL(urlString)
        val connection = url.openConnection()
        try {
            val httpConnection = connection as HttpURLConnection
            httpConnection.requestMethod = "GET"
            httpConnection.connect()
            if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.inputStream
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            println("downloadImage$ex")
        }
        return stream
    }

    fun getCheckedWxBitmap(base64: String?): Bitmap? {
        return resizeBitmapToMaxSize(base64ToBitmap(base64 ?: ""), 10 * 1024 * 1024)
    }

    fun resizeBitmapToMaxSize(original: Bitmap?, maxBytes: Int): Bitmap? {
        if (original == null) return original
        // 原始宽高
        val width = original.width
        val height = original.height

        // 当前 Bitmap 大小
        val baos = ByteArrayOutputStream()
        original.compress(CompressFormat.JPEG, 100, baos)
        val currentSize = baos.toByteArray().size

        // 如果当前大小已小于 maxBytes，直接返回
        if (currentSize <= maxBytes) {
            return original
        }

        // 计算缩放比例
        val scale = sqrt(maxBytes.toDouble() / currentSize)

        // 新的宽高
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        // 缩放 Bitmap
        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
    }

    fun saveImageToGallery(bmp: Bitmap, context: Context) {
        val appDir: File = File(getDCIM())
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 通知图库更新
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + getDCIM())
            )
        )
    }

    /**
     * 获取相册路径
     */
    private fun getDCIM(): String {
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            return ""
        }
        var path = Environment.getExternalStorageDirectory().path + "/dcim/"
        if (File(path).exists()) {
            return path
        }
        path = Environment.getExternalStorageDirectory().path + "/DCIM/"
        val file = File(path)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return ""
            }
        }
        return path
    }

    fun compressImage(image: Bitmap, targetSize: Int): Bitmap {
        return compressByMatrix(image, targetSize)
    }

    private fun compressByMatrix(image: Bitmap, targetSize: Int): Bitmap {
        val size = targetSize
        val out = ByteArrayOutputStream()
        //先不压缩，先获取image数据流放在out里面，
        image.compress(CompressFormat.JPEG, 100, out)

        //100kb/原始大小 ，再开平方，先把宽
        val zoom = sqrt((size * 1024 / out.toByteArray().size.toFloat()).toDouble()).toFloat()

        val matrix = Matrix()
        matrix.setScale(zoom, zoom)

        //缩放比例 zoom= 100*1024/origin 开平方
        var result = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)


        //压缩质量到75
        out.reset()
        result.compress(CompressFormat.JPEG, 75, out)

        //如果还是大于100KB那就进行 缩小宽高，循环压缩
        while (out.toByteArray().size > size * 1024) {
            matrix.setScale(0.9f, 0.9f)
            result = Bitmap.createBitmap(result, 0, 0, result.width, result.height, matrix, true)
            out.reset()
            result.compress(CompressFormat.JPEG, 75, out)
        }
        return result
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String?): File? {
        val cacheDir = context.cacheDir // 获取缓存目录
        val file = File(cacheDir, fileName) // 生成目标文件

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            // 保存为 PNG 或 JPEG，注意 PNG 支持透明
            bitmap.compress(CompressFormat.PNG, 100, out)
            out.flush()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    fun uriToBitmap(mContext: Context, uri: Uri?): Bitmap? {
        if (uri == null) return null
        try {
            // 读取uri所在的图片
            val input = mContext.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(input)
            input!!.close()
            return bitmap
        } catch (e: java.lang.Exception) {
            Toast.makeText(mContext, "通过uri获取图片失败", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}
