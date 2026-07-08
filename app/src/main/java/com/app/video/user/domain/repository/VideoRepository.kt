package com.app.video.user.domain.repository

import com.app.video.user.domain.model.EpisodeItem
import com.app.video.user.domain.model.FavoriteVideoItem
import com.app.video.user.domain.model.VideoDetail
import com.app.video.user.domain.model.VideoItem
import kotlinx.coroutines.delay

/**
 * 新增视频内容仓库。
 *
 * V1 使用本地 Mock 数据支撑详情、播放、收藏三个页面；
 * V2 接后端时，保留方法签名并把内部数据源替换成 OkHttp 接口即可。
 */
class VideoRepository {

    suspend fun getVideoDetail(videoId: String, isFavorite: Boolean): VideoDetail {
        delay(180)
        val video = getVideoById(videoId)
        return VideoDetail(
            video = video,
            description = buildDescription(video),
            director = "云影工作室",
            actors = "林清、周然、许安、陈屿",
            area = areaText(video.categoryId),
            year = "2026",
            episodes = buildEpisodes(video),
            recommends = getRecommendVideos(video),
            favoriteCount = "${(video.id.hashCode().absoluteValue % 90) + 10}万",
            isFavorite = isFavorite
        )
    }

    suspend fun getEpisode(videoId: String, episodeId: String?): EpisodeItem {
        delay(80)
        val video = getVideoById(videoId)
        return buildEpisodes(video).firstOrNull { it.id == episodeId }
            ?: buildEpisodes(video).first()
    }

    suspend fun getFavoriteVideos(
        favoriteIds: List<String>,
        addedTimeProvider: (String) -> String
    ): List<FavoriteVideoItem> {
        delay(120)
        return favoriteIds.mapNotNull { id ->
            getAllVideos().firstOrNull { it.id == id }?.let { video ->
                FavoriteVideoItem(
                    video = video,
                    addedTime = addedTimeProvider(id).ifBlank { "刚刚收藏" }
                )
            }
        }
    }

    fun getVideoById(videoId: String): VideoItem {
        return getAllVideos().firstOrNull { it.id == videoId } ?: getAllVideos().first()
    }

    private fun getRecommendVideos(video: VideoItem): List<VideoItem> {
        val sameCategory = getAllVideos()
            .filter { it.categoryId == video.categoryId && it.id != video.id }
        val fallback = getAllVideos().filter { it.id != video.id }
        return (sameCategory + fallback).distinctBy { it.id }.take(4)
    }

    private fun buildEpisodes(video: VideoItem): List<EpisodeItem> {
        val episodeCount = when (video.categoryId) {
            "movie" -> 1
            "short" -> 20
            "show" -> 8
            else -> 12
        }
        return (1..episodeCount).map { index ->
            EpisodeItem(
                id = "${video.id}_ep_$index",
                videoId = video.id,
                episodeNo = index,
                title = if (episodeCount == 1) "正片" else "第 ${index} 集",
                duration = when (video.categoryId) {
                    "movie" -> 6_720_000L
                    "short" -> 180_000L
                    "show" -> 4_380_000L
                    else -> 2_580_000L
                },
                playUrl = samplePlayUrls[(index - 1) % samplePlayUrls.size],
                isVip = video.isVip && index > 2
            )
        }
    }

    private fun buildDescription(video: VideoItem): String {
        return "${video.title} 是云影视频 V1 内容模块的示例数据，展示标题、封面、简介、选集、推荐和收藏等完整详情页链路。当前播放地址使用固定 mp4，后续可替换为真实视频接口返回的 playUrl。"
    }

    private fun areaText(categoryId: String): String {
        return when (categoryId) {
            "anime" -> "日本"
            "movie" -> "欧美"
            "show" -> "大陆"
            "record" -> "大陆"
            else -> "大陆"
        }
    }

    private fun getAllVideos(): List<VideoItem> {
        return listOf(
            VideoItem("v_tv_001", "风起长安", "全 40 集", "", "tv", "电视剧", "45:20", "128万", "8.8", "热播", true),
            VideoItem("v_movie_001", "星河行动", "高燃科幻动作", "", "movie", "电影", "01:52:10", "86万", "8.5", "独播", false),
            VideoItem("v_show_001", "周末喜剧大会", "第 2026-07-06 期", "", "show", "综艺", "01:12:36", "52万", "8.2", "上新", false),
            VideoItem("v_anime_001", "云端少年", "更新至 18 集", "", "anime", "动漫", "24:00", "210万", "9.1", "高分", true),
            VideoItem("v_short_001", "闪婚日记", "80 集全", "", "short", "短剧", "02:30", "320万", "7.8", "短剧", false),
            VideoItem("v_record_001", "山海寻味", "全 8 集", "", "record", "纪录片", "36:00", "33万", "9.0", "高分", false),
            VideoItem("v_vip_001", "暗夜档案", "会员抢先看 6 集", "", "tv", "悬疑", "42:18", "73万", "8.7", "VIP", true),
            VideoItem("v_vip_002", "海岸线", "蓝光修复版", "", "movie", "电影", "01:38:09", "39万", "8.3", "会员", true),
            VideoItem("v_history_001", "昨日追剧", "看到第 12 集 18:20", "", "tv", "继续观看", "45:00", "继续", "8.6", "历史", false),
            VideoItem("v_history_002", "轻喜人生", "看到第 3 集 08:12", "", "show", "继续观看", "38:00", "继续", "8.0", "历史", false),
            VideoItem("c_tv_001", "风起长安", "全 40 集", "", "tv", "电视剧", "45:20", "128万", "8.8", "热播", true),
            VideoItem("c_tv_002", "月色归途", "更新至 24 集", "", "tv", "电视剧", "43:18", "94万", "8.4", "上新", false),
            VideoItem("c_movie_001", "星河行动", "高燃科幻动作", "", "movie", "电影", "01:52:10", "86万", "8.5", "独播", false),
            VideoItem("c_movie_002", "海岸线", "蓝光修复版", "", "movie", "电影", "01:38:09", "39万", "8.3", "会员", true),
            VideoItem("c_show_001", "周末喜剧大会", "第 2026-07-06 期", "", "show", "综艺", "01:12:36", "52万", "8.2", "上新", false),
            VideoItem("c_show_002", "青春练习册", "高光舞台合集", "", "show", "综艺", "58:30", "47万", "7.9", "精选", false),
            VideoItem("c_anime_001", "云端少年", "更新至 18 集", "", "anime", "动漫", "24:00", "210万", "9.1", "高分", true),
            VideoItem("c_record_001", "山海寻味", "全 8 集", "", "record", "纪录片", "36:00", "33万", "9.0", "高分", false),
            VideoItem("c_short_001", "闪婚日记", "80 集全", "", "short", "短剧", "02:30", "320万", "7.8", "短剧", false),
            VideoItem("c_short_002", "逆袭合伙人", "更新至 36 集", "", "short", "短剧", "03:00", "188万", "8.1", "短剧", true),
            VideoItem("sd_001", "闪婚日记", "先婚后爱高甜来袭", "", "short", "短剧", "02:30", "320万", "9.8", "爆款", false),
            VideoItem("sd_002", "逆袭合伙人", "小职员翻盘创业局", "", "short", "短剧", "03:00", "188万", "9.1", "更新", true),
            VideoItem("sd_003", "晚风不迟到", "都市治愈轻喜剧", "", "short", "短剧", "02:45", "96万", "8.3", "完结", false),
            VideoItem("sd_004", "将军的账本", "古风探案加轻喜", "", "short", "短剧", "03:10", "121万", "8.8", "新剧", true),
            VideoItem("sd_005", "重启人生计划", "低谷回归一路开挂", "", "short", "短剧", "02:50", "410万", "9.9", "高热", false),
            VideoItem("sd_006", "隔壁老板太会撩", "职场欢喜冤家", "", "short", "短剧", "03:05", "152万", "8.7", "甜宠", false),
            VideoItem("vip_001", "暗夜档案", "会员抢先看 6 集", "", "tv", "会员", "42:18", "73万", "8.7", "VIP", true),
            VideoItem("vip_002", "海岸线", "蓝光修复版", "", "movie", "会员", "01:38:09", "39万", "8.3", "会员", true),
            VideoItem("vip_003", "云端少年", "高分动漫会员专享", "", "anime", "会员", "24:00", "210万", "9.1", "独享", true)
        )
    }

    private val samplePlayUrls = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    )

    private val Int.absoluteValue: Int
        get() = if (this == Int.MIN_VALUE) Int.MAX_VALUE else kotlin.math.abs(this)
}
