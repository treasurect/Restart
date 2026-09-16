package com.treasure.restart.func.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.treasure.restart.base.BaseFragment
import com.treasure.restart.helper.AppRestartHelper
import com.treasure.restart.helper.LoginManager
import com.treasure.restart.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val profileAdapter = ProfileListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profile = viewModel.profile.value ?: return

        binding.profileRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.profileRecycler.adapter = profileAdapter
        profileAdapter.setProfile(profile)

        binding.ivProfileMenu.setOnClickListener {
            binding.profileDrawerLayout.openDrawer(binding.profileDrawer.root)
        }

        binding.profileDrawer.btnLogout.setOnClickListener {
            binding.profileDrawerLayout.closeDrawer(binding.profileDrawer.root)
            LoginManager.logout()
            AppRestartHelper.restart(requireContext())
        }

        binding.profilePager.adapter = ProfilePagerAdapter(this, viewModel.tabs)
        TabLayoutMediator(binding.profileTabs, binding.profilePager) { tab, position ->
            tab.text = viewModel.tabs[position]
        }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
