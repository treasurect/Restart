package com.treasure.restart.func.main.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.databinding.FragmentMessageBinding

class MessageFragment : BaseFragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messagePager.adapter = MessagePagerAdapter(this, viewModel.tabs)
        TabLayoutMediator(binding.messageTabs, binding.messagePager) { tab, position ->
            tab.text = viewModel.tabs[position]
        }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
