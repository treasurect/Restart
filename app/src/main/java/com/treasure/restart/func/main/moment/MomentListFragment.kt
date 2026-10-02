package com.treasure.restart.func.main.moment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.treasure.basic.SharedKey
import com.treasure.basic.helper.AppEvent
import com.treasure.basic.helper.AppEventManager
import com.treasure.basic.helper.AppRestartHelper
import com.treasure.basic.helper.SharePreferenceManager
import com.treasure.basic.utils.ToastUtils
import com.treasure.restart.R
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.func.media.ImagePreviewActivity
import kotlinx.coroutines.launch

class MomentListFragment : BaseFragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickyHeader: View
    private lateinit var stickyAvatar: ImageView
    private lateinit var stickyNickname: TextView
    private lateinit var adapter: MomentListAdapter

    private val viewModel: MomentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        initRecyclerView()
        initRefresh()
        observeData()
        observeAppEvents()

        viewModel.loadMomentList(refresh = true)
    }

    private fun initView(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.recyclerView)
        stickyHeader = view.findViewById(R.id.stickyHeader)
        stickyAvatar = view.findViewById(R.id.stickyAvatar)
        stickyNickname = view.findViewById(R.id.stickyNickname)
    }

    private fun initRecyclerView() {
        adapter = MomentListAdapter(mutableListOf()) { position, images ->
            openImagePreview(position, images)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.itemAnimator = null

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                updateStickyHeader()
                checkLoadMore()
            }
        })
    }

    private fun initRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMomentList(refresh = true)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.momentList.collect { list ->
                        adapter.replaceData(list)
                        updateStickyHeader()
                    }
                }

                launch {
                    viewModel.isLoading.collect { loading ->
                        swipeRefreshLayout.isRefreshing = loading && viewModel.currentPage.value == 2
                    }
                }

                launch {
                    viewModel.error.collect { message ->
                        if (isAdded) {
                            ToastUtils.show(message)
                        }
                    }
                }
            }
        }
    }

    private fun checkLoadMore() {
        if (viewModel.isLoading.value) return
        if (!viewModel.hasMore.value) return

        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        if (lastVisiblePosition >= adapter.itemCount - 3) {
            viewModel.loadMomentList(refresh = false)
        }
    }

    private fun updateStickyHeader() {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        if (firstVisiblePosition <= 0) {
            stickyHeader.visibility = View.GONE
            return
        }

        stickyHeader.visibility = View.VISIBLE

        stickyAvatar.setImageResource(R.drawable.ic_default_avatar)
        stickyNickname.text = "我的昵称"
    }

    private fun openImagePreview(position: Int, images: List<String>) {
        val intent = Intent(activity, ImagePreviewActivity::class.java)
        intent.putStringArrayListExtra(ImagePreviewActivity.EXTRA_IMAGES, ArrayList(images))
        intent.putExtra(ImagePreviewActivity.EXTRA_POSITION, position)
        startActivity(intent)
    }

    private fun observeAppEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                AppEventManager.events.collect {
                    when (it) {
                        AppEvent.MomentPublishSuccess -> {
                            viewModel.loadMomentList(refresh = true)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }
}
