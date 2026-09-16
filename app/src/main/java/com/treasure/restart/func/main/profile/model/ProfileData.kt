package com.treasure.restart.func.main.profile.model

data class ProfileData(
    val avatarText: String,
    val nickname: String,
    val userId: String,
    val bio: String,
    val stats: List<Pair<String, String>>,
    val actions: List<Pair<String, String>>
)
