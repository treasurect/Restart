package com.treasure.restart.bean

import com.google.gson.annotations.SerializedName

data class MomentListResponse(
    val page: Int,
    val total: Long,
    val momentList: List<MomentItem>
)

data class MomentItem(
    val id: Long,
    val userId: Long,
    val username: String,
    val content: String?,
    val location: String?,
    @SerializedName(value = "moment_images", alternate = ["images"])
    val images: List<String>? = emptyList(),
    val createdAt: String
)
