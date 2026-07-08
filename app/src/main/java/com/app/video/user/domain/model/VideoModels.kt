package com.app.video.user.domain.model

/**
 * 视频详情页的完整数据模型，承载详情、选集、推荐和收藏状态。
 */
data class VideoDetail(
    val video: VideoItem,
    val description: String,
    val director: String,
    val actors: String,
    val area: String,
    val year: String,
    val episodes: List<EpisodeItem>,
    val recommends: List<VideoItem>,
    val favoriteCount: String,
    val isFavorite: Boolean
)

/**
 * 播放页和详情页共用的选集模型。
 */
data class EpisodeItem(
    val id: String,
    val videoId: String,
    val episodeNo: Int,
    val title: String,
    val duration: Long,
    val playUrl: String,
    val isVip: Boolean
)

/**
 * 收藏列表展示模型，V1 先记录本地收藏时间。
 */
data class FavoriteVideoItem(
    val video: VideoItem,
    val addedTime: String
)
