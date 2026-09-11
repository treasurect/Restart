package com.treasure.basic.utils

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.treasure.basic.helper.CommonCallback
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object FileUtils {

    /** 判断文件是否存在 */
    fun isFileExists(path: String): Boolean {
        return File(path).exists()
    }

    /** 获取文件大小（单位字节） */
    fun getFileSize(path: String): Long {
        return File(path).takeIf { it.exists() }?.length() ?: 0L
    }

    /** 删除文件或文件夹 */
    fun deleteFile(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) return false
        if (file.isFile) return file.delete()
        file.listFiles()?.forEach { deleteFile(it.absolutePath) }
        return file.delete()
    }

    /** 写入内容到文件（自动创建） */
    fun writeToFile(content: String, filePath: String): Boolean {
        return try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /** 打开系统文件选择器（推荐在 ActivityResult 中调用） */
    fun openFilePicker(mimeType: String = "*/*"): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
        }
    }

    /** Android 11+ 是否拥有 MANAGE_EXTERNAL_STORAGE 权限 */
    fun hasManageExternalStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else true
    }

    /** 检查是否拥有读取存储权限（Android 13 分媒体类型） */
    fun hasStoragePermission(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Android 10 默认无需申请读权限
                true
            }

            else -> {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    /** 跳转到设置页面开启管理权限 */
    fun goToManageStoragePermissionPage(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                // fallback
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            when {
                uri.authority == "com.android.externalstorage.documents" -> {
                    val parts = docId.split(":")
                    val type = parts[0]
                    val path = parts[1]
                    if (type.equals("primary", true)) {
                        return "${Environment.getExternalStorageDirectory()}/$path"
                    }
                }

                uri.authority == "com.android.providers.downloads.documents" -> {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLongOrNull() ?: return null
                    )
                    return getDataColumn(context, contentUri)
                }

                uri.authority == "com.android.providers.media.documents" -> {
                    val parts = docId.split(":")
                    val mediaType = parts[0]
                    val id = parts[1]
                    val contentUri = when (mediaType) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    return contentUri?.let {
                        getDataColumn(context, it, "_id=?", arrayOf(id))
                    }
                }
            }
        }
        // content://
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri)
        }
        // file://
        if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    /** 获取 content:// uri 对应的 _data 列路径 */
    private fun getDataColumn(
        context: Context, uri: Uri, selection: String? = null, selectionArgs: Array<String>? = null
    ): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

    /** 拷贝 SAF 文件到缓存目录 */
    fun copyFileToCache(context: Context, uri: Uri, fileName: String? = null): File? {
        return try {
            val name = fileName ?: "tmp_${System.currentTimeMillis()}"
            val cacheFile = File(context.cacheDir, name)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(cacheFile).use { output ->
                    input.copyTo(output)
                }
            }
            cacheFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** 分享文件（通过系统分享弹窗） */
    fun shareFileViaIntent(context: Context, file: File, mimeType: String = "*/*") {
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "分享文件"))
    }

    // 获取 Uri 对应的文件后缀名（可选）
    private fun getFileSuffix(context: Context, uri: Uri): String? {
        val mimeType = context.contentResolver.getType(uri) ?: return null

        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(mimeType)
    }

    fun isExists(@Nullable file: File?): Boolean {
        return file != null && file.exists()
    }

    fun doCompress(
        context: Context?,
        originFile: File?,
        targetSize: Int,
        cb: CommonCallback<File?>
    ) {
        Luban.with(context) //context
            .load(originFile) // 需要压缩的图片file
            .ignoreBy(targetSize) //压缩率 ，默认100
            //压缩后文件目录， 如果希望替换原图就不要这行
            .filter { path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(file: File?) {
                    cb.onContinue(file)
                }

                override fun onError(e: Throwable?) {
                    cb.onContinue(originFile)
                }
            }).launch()
    }

    fun doCompress(
        context: Context?,
        files: ArrayList<File>,
        targetSize: Int,
        cb: CommonCallback<ArrayList<File?>>
    ) {
        //支持批量 但是回调 一个一个来，失败了找不到原始文件 只能foreach
        val list = ArrayList<File?>()
        val (matched, files2) = files.partition { it.length() > targetSize * 1024 }
        list.addAll(files2)
        if (files.size == files2.size) {
            cb.onContinue(list)
        } else matched.forEach {
            doCompress(context, it, targetSize, CommonCallback {
                list.add(it)
                if (list.size == files.size) cb.onContinue(list)
            })
        }
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        var file: File? = null

        if ("file" == uri.scheme) {
            file = File(uri.path)
        } else if ("content" == uri.scheme) {
            try {
                // 1. 打开输入流
                val inputStream = context.contentResolver.openInputStream(uri)

                // 2. 创建临时文件（缓存目录）
                val suffix = getFileSuffix(context, uri)
                file = File(
                    context.cacheDir,
                    System.currentTimeMillis().toString() + (if (suffix != null) ".$suffix" else "")
                )

                // 3. 写入临时文件
                val outputStream: OutputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var len: Int
                while ((inputStream!!.read(buffer).also { len = it }) != -1) {
                    outputStream.write(buffer, 0, len)
                }

                outputStream.close()
                inputStream!!.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        return file
    }
}
