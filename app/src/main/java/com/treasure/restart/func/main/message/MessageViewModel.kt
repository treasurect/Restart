package com.treasure.restart.func.main.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treasure.restart.func.main.message.model.MessageListItem

class MessageViewModel : ViewModel() {

    val tabs = listOf("赞和收藏", "新增关注", "评论和@")

    private val _items = MutableLiveData<List<MessageListItem>>()
    val items: LiveData<List<MessageListItem>> = _items

    fun loadTab(tabIndex: Int) {
        _items.value = when (tabIndex) {
            0 -> listOf(
                MessageListItem.Activity(
                    title = "活动消息",
                    content = "@泰山球迷，快来山东泰山球迷圈集合！",
                    time = "07-31",
                    iconLabel = "子"
                ),
                MessageListItem.Section("你可能感兴趣的人"),
                suggestionItem("懒羊羊的房车生活（陪读日常）", "汽车内容热门作者", "懒"),
                suggestionItem("长春房车（收售）胖丫", "汽车内容热门作者", "长"),
                suggestionItem("君君辅食记", "母婴内容热门作者", "君"),
                suggestionItem("雅清房车日记", "汽车内容热门作者", "雅"),
                suggestionItem("伊蒂哈德东宫二代（在逃版", "体育运动内容热门作者", "伊"),
                suggestionItem("夜空中最璀璨的星", "体育运动内容热门作者", "夜"),
                suggestionItem("麦其不麦其", "时尚内容热门作者", "麦")
            )
            1 -> listOf(
                MessageListItem.Section("新增关注"),
                suggestionItem("懒羊羊的房车生活（陪读日常）", "汽车内容热门作者", "懒"),
                suggestionItem("长春房车（收售）胖丫", "汽车内容热门作者", "长"),
                suggestionItem("雅清房车日记", "汽车内容热门作者", "雅")
            )
            else -> listOf(
                MessageListItem.Activity(
                    title = "评论和@",
                    content = "@泰山球迷，快来山东泰山球迷圈集合！",
                    time = "07-31",
                    iconLabel = "@"
                ),
                MessageListItem.Activity(
                    title = "收到的评论",
                    content = "这条笔记写得太好了，收藏了。",
                    time = "昨天",
                    iconLabel = "评"
                )
            )
        }
    }

    private fun suggestionItem(nickname: String, description: String, avatar: String) =
        MessageListItem.Suggestion(nickname, description, avatar)
}
