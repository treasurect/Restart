package com.treasure.restart.func.main

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.treasure.basic.ContextHolder
import com.treasure.basic.base.BaseViewModel
import com.treasure.basic.helper.LogHelper
import com.treasure.basic.network.ApiResult
import com.treasure.basic.network.asResult
import com.treasure.basic.utils.ToastUtils
import com.treasure.restart.network.repository.FileRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class UploadViewModel(application: Application) : BaseViewModel(application) {
    private val response = FileRepository()

    fun uploadImage(uris: List<Uri>) {
        viewModelScope.launch {
            try {
                val parts = uris.map { uri ->
                    createMultipart(
                        ContextHolder.app(),
                        uri
                    )
                }
                LogHelper.i("开始上传 -> Uri -> ${uris.joinToString { it.toString() }}")
                LogHelper.i("Multipart 创建完成")

                response.uploadFiles(parts).asResult().collect { result ->
                    when (result) {
                        is ApiResult.Error -> {
                            ToastUtils.show("上传失败 -> ${result.msg}")
                        }

                        is ApiResult.Success<*> -> {
                            ToastUtils.show("上传成功 - > ${result.data}")
                        }
                    }
                }

            } catch (e: Exception) {
                LogHelper.e("上传异常 -> ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun createMultipart(context: Context, uri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: throw IOException("无法读取文件")
        LogHelper.i("上传文件 -> path=${file.absolutePath}, size=${file.length()}")
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("files", file.name, requestBody)
    }

}