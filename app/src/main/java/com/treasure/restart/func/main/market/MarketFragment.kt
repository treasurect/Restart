package com.treasure.restart.func.main.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.databinding.FragmentMarketBinding

class MarketFragment : BaseFragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: com.treasure.restart.func.main.market.MarketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMarketTitle.text = viewModel.title.value
        binding.tvMarketEmpty.text = viewModel.emptyHint.value
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
