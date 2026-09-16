package com.treasure.restart.func.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treasure.restart.func.main.profile.model.ProfileData

class ProfileViewModel : ViewModel() {

    val tabs = listOf("笔记", "收藏", "赞过")

    val profile = MutableLiveData(
        ProfileData(
            avatarText = "踩",
            nickname = "踩单车",
            userId = "小红书号：26248013403",
            bio = "点击这里，填写简介",
            stats = listOf(
                "0" to "关注",
                "0" to "粉丝",
                "0" to "获赞与收藏"
            ),
            actions = listOf(
                "① 浏览记录" to "看过的笔记",
                "@ 钱包" to "查看详情"
            )
        )
    )
}
