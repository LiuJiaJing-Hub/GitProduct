package com.app.video.user.page.home

import com.app.video.user.domain.model.BannerItem
import com.app.video.user.domain.model.ChannelItem
import com.app.video.user.domain.model.HomeData
import com.app.video.user.domain.model.HomeSection
import com.app.video.user.domain.model.VideoItem
import kotlinx.coroutines.delay

class HomeRepository {

    suspend fun getHomeData(): HomeData {
        delay(250)

        val hotVideos = listOf(
            VideoItem("v_tv_001", "风起长安", "全 40 集", "", "tv", "电视剧", "45:20", "128万", "8.8", "热播", true),
            VideoItem("v_movie_001", "星河行动", "高燃科幻动作", "", "movie", "电影", "01:52:10", "86万", "8.5", "独播", false),
            VideoItem("v_show_001", "周末喜剧大会", "第 2026-07-06 期", "", "show", "综艺", "01:12:36", "52万", "8.2", "上新", false),
            VideoItem("v_anime_001", "云端少年", "更新至 18 集", "", "anime", "动漫", "24:00", "210万", "9.1", "高分", true),
            VideoItem("v_short_001", "闪婚日记", "80 集全", "", "short", "短剧", "02:30", "320万", "7.8", "短剧", false),
            VideoItem("v_record_001", "山海寻味", "全 8 集", "", "record", "纪录片", "36:00", "33万", "9.0", "高分", false)
        )

        val vipVideos = listOf(
            VideoItem("v_vip_001", "暗夜档案", "会员抢先看 6 集", "", "tv", "悬疑", "42:18", "73万", "8.7", "VIP", true),
            VideoItem("v_vip_002", "海岸线", "蓝光修复版", "", "movie", "电影", "01:38:09", "39万", "8.3", "会员", true)
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
                VideoItem("v_history_001", "昨日追剧", "看到第 12 集 18:20", "", "tv", "继续观看", "45:00", "继续", "8.6", "历史", false),
                VideoItem("v_history_002", "轻喜人生", "看到第 3 集 08:12", "", "show", "继续观看", "38:00", "继续", "8.0", "历史", false)
            ),
            sections = listOf(
                HomeSection("hot", "正在热播", "grid", hotVideos),
                HomeSection("vip", "VIP 精选", "grid", vipVideos)
            )
        )
    }
}
