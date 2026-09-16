package com.treasure.restart.func.main.message

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MessagePagerAdapter(
    fragment: Fragment,
    private val titles: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return MessageListFragment.newInstance(position)
    }
}
