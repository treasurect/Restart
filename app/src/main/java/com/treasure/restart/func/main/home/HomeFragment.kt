package com.treasure.restart.func.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.treasure.restart.R
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.databinding.FragmentHomeBinding
import com.treasure.restart.func.chat.ChatActivity

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeChat.setOnClickListener {
            startActivity(Intent(requireContext(), ChatActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.homeCategoryPager.adapter =
            HomeCategoryPagerAdapter(
                this,
                viewModel.categories
            )
        TabLayoutMediator(binding.homeCategoryTabs, binding.homeCategoryPager) { tab, position ->
            tab.text = viewModel.categories[position]
        }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
