package com.treasure.restart

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.treasure.restart.base.BaseActivity
import com.treasure.restart.helper.LoginManager
import com.treasure.restart.databinding.ActivityMainBinding
import com.treasure.restart.func.login.LoginFragment
import com.treasure.restart.func.main.home.HomeFragment
import com.treasure.restart.func.main.market.MarketFragment
import com.treasure.restart.func.main.message.MessageFragment
import com.treasure.restart.func.main.profile.ProfileFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var loggedIn = LoginManager.isLoggedIn()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPager.isUserInputEnabled = false
        binding.mainPager.adapter = MainPagerAdapter(this)

        binding.navHome.setOnClickListener {
            binding.mainPager.setCurrentItem(0, false)
        }
        binding.navMarket.setOnClickListener {
            binding.mainPager.setCurrentItem(1, false)
        }
        binding.navPublish.setOnClickListener {
            Toast.makeText(this, "发布功能建设中", Toast.LENGTH_SHORT).show()
        }
        binding.navMessage.setOnClickListener {
            binding.mainPager.setCurrentItem(2, false)
        }
        binding.navMe.setOnClickListener {
            binding.mainPager.setCurrentItem(3, false)
        }

        binding.mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateBottomNav(position)
            }
        })
        updateBottomNav(0)
    }

    override fun onResume() {
        super.onResume()
        val nowLoggedIn = LoginManager.isLoggedIn()
        if (nowLoggedIn != loggedIn) {
            loggedIn = nowLoggedIn
            binding.mainPager.adapter = MainPagerAdapter(this)
            binding.mainPager.setCurrentItem(3, false)
        }
    }

    private fun updateBottomNav(selected: Int) {
        val selectedColor = Color.rgb(255, 36, 66)
        val normalColor = Color.rgb(102, 102, 102)
        val navTexts = listOf(
            binding.navHome,
            binding.navMarket,
            binding.navMessage,
            binding.navMe
        )

        navTexts.forEachIndexed { index, textView ->
            val color = if (index == selected) selectedColor else normalColor
            textView.compoundDrawableTintList = ColorStateList.valueOf(color)
            textView.setTextColor(color)
        }
    }

    class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> MarketFragment()
                2 -> MessageFragment()
                else -> if (LoginManager.isLoggedIn()) ProfileFragment() else LoginFragment()
            }
        }
    }
}
