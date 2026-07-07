package com.app.video.user.domain.model

data class BannerItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val imageUrl: String,
    val targetVideoId: String,
    val tag: String
)

data class ChannelItem(
    val id: String,
    val name: String
)

data class VideoItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val coverUrl: String,
    val category: String,
    val duration: String,
    val playCount: String,
    val score: String,
    val tag: String,
    val isVip: Boolean
)

data class HomeSection(
    val id: String,
    val title: String,
    val type: String,
    val videos: List<VideoItem>
)

data class HomeData(
    val banners: List<BannerItem>,
    val channels: List<ChannelItem>,
    val continueWatching: List<VideoItem>,
    val sections: List<HomeSection>
)
