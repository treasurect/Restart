package com.treasure.restart.func.main.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.databinding.FragmentMessageListBinding

class MessageListFragment : BaseFragment() {

    private var _binding: FragmentMessageListBinding? = null
    private val binding get() = _binding!!
    private val tabIndex: Int
        get() = arguments?.getInt(ARG_TAB_INDEX) ?: 0
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MessageAdapter()
        binding.messageList.adapter = adapter

        viewModel.loadTab(tabIndex)
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_TAB_INDEX = "tab_index"

        fun newInstance(tabIndex: Int): MessageListFragment {
            return MessageListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAB_INDEX, tabIndex)
                }
            }
        }
    }
}
