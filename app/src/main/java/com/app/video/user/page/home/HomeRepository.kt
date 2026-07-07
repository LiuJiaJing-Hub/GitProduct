package com.app.video.user.page.home

import com.app.video.user.domain.model.BannerItem
import com.app.video.user.domain.model.ChannelItem
import com.app.video.user.domain.model.HomeData
import com.app.video.user.domain.model.HomeSection
import com.app.video.user.domain.model.VideoItem
import kotlinx.coroutines.delay

class HomeRepository {
 //   suspend 挂起函数，表示该函数可以被挂起，从而允许其他协程在等待I/O操作完成时执行
    suspend fun getHomeData(): HomeData {
        delay(250)

        val hotVideos = listOf(
            VideoItem(
                id = "v_tv_001",
                title = "风起长安",
                subTitle = "全 40 集",
                coverUrl = "",
                category = "电视剧",
                duration = "45:20",
                playCount = "128万",
                score = "8.8",
                tag = "热播",
                isVip = true
            ),
            VideoItem(
                id = "v_movie_001",
                title = "星河行动",
                subTitle = "高燃科幻动作",
                coverUrl = "",
                category = "电影",
                duration = "01:52:10",
                playCount = "86万",
                score = "8.5",
                tag = "独播",
                isVip = false
            ),
            VideoItem(
                id = "v_show_001",
                title = "周末喜剧夜",
                subTitle = "第 2026-07-06 期",
                coverUrl = "",
                category = "综艺",
                duration = "01:12:36",
                playCount = "52万",
                score = "8.2",
                tag = "上新",
                isVip = false
            ),
            VideoItem(
                id = "v_anime_001",
                title = "云端少年",
                subTitle = "更新至 18 集",
                coverUrl = "",
                category = "动漫",
                duration = "24:00",
                playCount = "210万",
                score = "9.1",
                tag = "高分",
                isVip = true
            )
        )

        val vipVideos = listOf(
            VideoItem(
                id = "v_vip_001",
                title = "暗夜档案",
                subTitle = "会员抢先看 6 集",
                coverUrl = "",
                category = "悬疑",
                duration = "42:18",
                playCount = "73万",
                score = "8.7",
                tag = "VIP",
                isVip = true
            ),
            VideoItem(
                id = "v_vip_002",
                title = "海岸线",
                subTitle = "蓝光修复版",
                coverUrl = "",
                category = "电影",
                duration = "01:38:09",
                playCount = "39万",
                score = "8.3",
                tag = "会员",
                isVip = true
            )
        )

        return HomeData(
            banners = listOf(
                BannerItem(
                    id = "banner_001",
                    title = "风起长安 正在热播",
                    subTitle = "权谋、悬疑、群像交织，会员抢先看最新剧集",
                    imageUrl = "",
                    targetVideoId = "v_tv_001",
                    tag = "今日推荐"
                )
            ),
            channels = listOf(
                ChannelItem("recommend", "推荐"),
                ChannelItem("tv", "电视剧"),
                ChannelItem("movie", "电影"),
                ChannelItem("show", "综艺"),
                ChannelItem("anime", "动漫"),
                ChannelItem("short", "短剧"),
                ChannelItem("record", "纪录片")
            ),
            continueWatching = listOf(
                VideoItem(
                    id = "v_history_001",
                    title = "昨日追剧",
                    subTitle = "看到第 12 集 18:20",
                    coverUrl = "",
                    category = "继续观看",
                    duration = "45:00",
                    playCount = "继续",
                    score = "8.6",
                    tag = "历史",
                    isVip = false
                ),
                VideoItem(
                    id = "v_history_002",
                    title = "轻喜人生",
                    subTitle = "看到第 3 集 08:12",
                    coverUrl = "",
                    category = "继续观看",
                    duration = "38:00",
                    playCount = "继续",
                    score = "8.0",
                    tag = "历史",
                    isVip = false
                )
            ),
            sections = listOf(
                HomeSection(
                    id = "hot",
                    title = "正在热播",
                    type = "grid",
                    videos = hotVideos
                ),
                HomeSection(
                    id = "vip",
                    title = "VIP 精选",
                    type = "grid",
                    videos = vipVideos
                )
            )
        )
    }
}
