package com.treasure.restart.func.main.moment

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.treasure.basic.ContextHolder
import com.treasure.basic.base.BaseViewModel
import com.treasure.basic.helper.LogHelper
import com.treasure.basic.network.ApiResult
import com.treasure.basic.network.asResult
import com.treasure.restart.network.repository.FileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class FileUploadViewModel(application: Application) : BaseViewModel(application) {
    private val response = FileRepository()

    private val _uploadResult = MutableSharedFlow<List<String>?>()
    val uploadResult = _uploadResult.asSharedFlow()
    fun uploadImage(uris: List<Uri>) {
        viewModelScope.launch {
            try {
                LogHelper.i("开始上传 -> Uri -> ${uris.joinToString { it.toString() }}")
                val parts = uris.map { uri ->
                    createMultipart(ContextHolder.app(), uri)
                }
                response.uploadFiles(parts).asResult().collect { result ->
                    _uploadResult.emit(if (result is ApiResult.Success) result.data else null)
                    when (result) {
                        is ApiResult.Error -> {
                            LogHelper.i("图片上传失败 -> ${result.msg}")
                        }

                        is ApiResult.Success -> {
                            LogHelper.i("图片上传成功 - > ${result.data}")
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