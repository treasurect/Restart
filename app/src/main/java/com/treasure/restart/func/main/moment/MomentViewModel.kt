package com.treasure.restart.func.main.moment

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.treasure.basic.base.BaseViewModel
import com.treasure.basic.network.ApiResult
import com.treasure.basic.network.asResult
import com.treasure.restart.bean.MomentItem
import com.treasure.restart.bean.MomentListResponse
import com.treasure.restart.bean.MomentPublishRequest
import com.treasure.restart.network.repository.MomentRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MomentViewModel(application: Application) : BaseViewModel(application) {

    private val repository = MomentRepository()

    companion object {
        private const val PAGE_SIZE = 10
    }

    // ==================== 发布朋友圈 ====================

    private val _publishResult = MutableSharedFlow<Long?>()
    val publishResult = _publishResult.asSharedFlow()

    fun publishMoment(content: String, location: String = "不公开", imageUrls: List<String> = emptyList()) {
        viewModelScope.launch {
            val request = MomentPublishRequest(
                content = content,
                location = location,
                images = imageUrls
            )

            repository.publishMoment(request).asResult().collectLatest {
                _publishResult.emit(if (it is ApiResult.Success) it.data else null)
            }
        }
    }

    // ==================== 朋友圈列表 ====================

    private val _momentList = MutableStateFlow<List<MomentItem>>(emptyList())
    val momentList = _momentList.asStateFlow()

    private val _total = MutableStateFlow(0L)
    val total = _total.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    /**
     * 是否正在进行网络请求
     * 防止快速滑动时重复请求
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * 是否正在下拉刷新
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    /**
     * 是否正在加载下一页
     */
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    /**
     * 是否还有下一页
     */
    private val _hasMore = MutableStateFlow(true)
    val hasMore = _hasMore.asStateFlow()

    /**
     * 请求错误
     */
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    /**
     * 加载朋友圈列表
     *
     * @param refresh true 重新从第一页加载
     * @param false 加载下一页
     */
    fun loadMomentList(refresh: Boolean = false) {
        if (_isLoading.value) return
        if (!refresh && !_hasMore.value) return

        viewModelScope.launch {
            _isLoading.value = true

            if (refresh) {
                _isRefreshing.value = true
            } else {
                _isLoadingMore.value = true
            }

            val page = if (refresh) 1 else _currentPage.value

            try {
                repository.getMomentList(page, PAGE_SIZE).asResult().collectLatest {response->
                    when(response) {
                        is ApiResult.Success -> {
                            val data = response.data ?: MomentListResponse(0, 0L, emptyList())
                            _total.value = data.total
                            if (refresh) {
                                _momentList.value = data.momentList
                            } else {
                                val oldList = _momentList.value
                                _momentList.value = oldList + data.momentList
                            }

                            // 下一次请求的页码
                            _currentPage.value = data.page + 1

                            // 当前已经加载的数据数量
                            val loadedCount = _momentList.value.size

                            // 是否还有下一页
                            _hasMore.value = loadedCount < data.total
                        }
                        is ApiResult.Error -> {
                            _error.emit(response.msg.ifBlank { "加载失败" })
                        }
                    }
                }


            } catch (e: Exception) {
                _error.emit(e.message ?: "网络请求失败")
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
                _isLoadingMore.value = false
            }
        }
    }

    /**
     * 清空朋友圈数据
     *
     * 一般不需要主动调用。
     * 如果以后切换账号，可以使用。
     */
    fun clearMomentList() {
        _momentList.value = emptyList()
        _total.value = 0L
        _currentPage.value = 1
        _hasMore.value = true
    }
}