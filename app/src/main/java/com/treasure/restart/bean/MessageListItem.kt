package com.treasure.restart.bean

sealed class MessageListItem {
    data class Section(val title: String) : MessageListItem()
    data class Activity(
        val title: String,
        val content: String,
        val time: String,
        val iconLabel: String
    ) : MessageListItem()

    data class Suggestion(
        val nickname: String,
        val description: String,
        val avatarLabel: String
    ) : MessageListItem()
}