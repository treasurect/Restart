package com.treasure.restart.bean

data class LoginResponse(
    val nickname: String,
    val token: String,
    val userId: Int,
    val username: String
)