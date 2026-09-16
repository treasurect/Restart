package com.treasure.restart.func.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.databinding.FragmentHomeTabBinding

class HomeTabFragment : BaseFragment() {

    private var _binding: FragmentHomeTabBinding? = null
    private val binding get() = _binding!!
    private val category: String
        get() = arguments?.getString(ARG_CATEGORY).orEmpty()
    private val parentViewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (category == "推荐") {
            binding.feedList.layoutManager = LinearLayoutManager(requireContext())
            val adapter = FeedAdapter()
            binding.feedList.adapter = adapter
            parentViewModel.feedItems.observe(viewLifecycleOwner) { items ->
                adapter.submitList(items)
            }
        } else {
            binding.feedList.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
            binding.emptyView.text = "$category 内容建设中"
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): HomeTabFragment {
            return HomeTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }
}
