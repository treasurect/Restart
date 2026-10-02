package com.treasure.restart.bean

data class MomentPublishRequest(
    val content: String?,
    val location: String?,
    val images: List<String>
)