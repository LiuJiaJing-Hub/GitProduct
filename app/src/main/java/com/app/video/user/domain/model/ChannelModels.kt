package com.app.video.user.domain.model

data class FilterOption(
    val id: String,
    val name: String
)

data class ChannelSelection(
    val categoryId: String = "all",
    val areaId: String = "all",
    val yearId: String = "all",
    val sortId: String = "hot"
)

data class ChannelVideoItem(
    val video: VideoItem,
    val categoryId: String,
    val areaId: String,
    val yearId: String,
    val typeId: String,
    val hotScore: Int,
    val publishTime: Long,
    val ratingScore: Double
)

data class ChannelData(
    val categories: List<FilterOption>,
    val areas: List<FilterOption>,
    val years: List<FilterOption>,
    val sorts: List<FilterOption>,
    val videos: List<ChannelVideoItem>
)
