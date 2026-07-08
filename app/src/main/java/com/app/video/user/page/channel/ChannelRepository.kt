package com.app.video.user.page.channel

import com.app.video.user.domain.model.ChannelData
import com.app.video.user.domain.model.ChannelVideoItem
import com.app.video.user.domain.model.FilterOption
import com.app.video.user.domain.model.VideoItem
import kotlinx.coroutines.delay

class ChannelRepository {

    suspend fun getChannelData(): ChannelData {
        delay(200)
        return ChannelData(
            categories = listOf(
                FilterOption("all", "全部"),
                FilterOption("tv", "电视剧"),
                FilterOption("movie", "电影"),
                FilterOption("show", "综艺"),
                FilterOption("anime", "动漫"),
                FilterOption("record", "纪录片"),
                FilterOption("short", "短剧")
            ),
            areas = listOf(
                FilterOption("all", "全部"),
                FilterOption("cn", "大陆"),
                FilterOption("kr", "韩国"),
                FilterOption("jp", "日本"),
                FilterOption("eu", "欧美"),
                FilterOption("th", "泰国")
            ),
            years = listOf(
                FilterOption("all", "全部"),
                FilterOption("2026", "2026"),
                FilterOption("2025", "2025"),
                FilterOption("2024", "2024"),
                FilterOption("older", "更早")
            ),
            sorts = listOf(
                FilterOption("hot", "最热"),
                FilterOption("new", "最新"),
                FilterOption("score", "评分最高")
            ),
            videos = buildVideos()
        )
    }

    private fun buildVideos(): List<ChannelVideoItem> {
        return listOf(
            createVideo("c_tv_001", "风起长安", "全 40 集", "电视剧", "45:20", "128万", "8.8", "热播", true, "tv", "cn", "2026", "costume", 98, 20260701L, 8.8),
            createVideo("c_tv_002", "月色归途", "更新至 24 集", "电视剧", "43:18", "94万", "8.4", "上新", false, "tv", "cn", "2025", "romance", 86, 20250618L, 8.4),
            createVideo("c_movie_001", "星河行动", "高燃科幻动作", "电影", "01:52:10", "86万", "8.5", "独播", false, "movie", "eu", "2026", "action", 91, 20260620L, 8.5),
            createVideo("c_movie_002", "海岸线", "蓝光修复版", "电影", "01:38:09", "39万", "8.3", "会员", true, "movie", "jp", "2024", "story", 76, 20240512L, 8.3),
            createVideo("c_show_001", "周末喜剧大会", "第 2026-07-06 期", "综艺", "01:12:36", "52万", "8.2", "上新", false, "show", "cn", "2026", "comedy", 82, 20260706L, 8.2),
            createVideo("c_show_002", "青春练习册", "高光舞台合集", "综艺", "58:30", "47万", "7.9", "精选", false, "show", "kr", "2025", "music", 74, 20250508L, 7.9),
            createVideo("c_anime_001", "云端少年", "更新至 18 集", "动漫", "24:00", "210万", "9.1", "高分", true, "anime", "jp", "2026", "adventure", 99, 20260630L, 9.1),
            createVideo("c_record_001", "山海寻味", "全 8 集", "纪录片", "36:00", "33万", "9.0", "高分", false, "record", "cn", "2024", "food", 72, 20240402L, 9.0),
            createVideo("c_short_001", "闪婚日记", "80 集全", "短剧", "02:30", "320万", "7.8", "短剧", false, "short", "cn", "2026", "romance", 96, 20260703L, 7.8),
            createVideo("c_short_002", "逆袭合伙人", "更新至 36 集", "短剧", "03:00", "188万", "8.1", "短剧", true, "short", "cn", "2025", "city", 88, 20251111L, 8.1)
        )
    }

    private fun createVideo(
        id: String,
        title: String,
        subTitle: String,
        category: String,
        duration: String,
        playCount: String,
        score: String,
        tag: String,
        isVip: Boolean,
        categoryId: String,
        areaId: String,
        yearId: String,
        typeId: String,
        hotScore: Int,
        publishTime: Long,
        ratingScore: Double
    ): ChannelVideoItem {
        return ChannelVideoItem(
            video = VideoItem(
                id = id,
                title = title,
                subTitle = subTitle,
                coverUrl = "",
                categoryId = categoryId,
                category = category,
                duration = duration,
                playCount = playCount,
                score = score,
                tag = tag,
                isVip = isVip
            ),
            categoryId = categoryId,
            areaId = areaId,
            yearId = yearId,
            typeId = typeId,
            hotScore = hotScore,
            publishTime = publishTime,
            ratingScore = ratingScore
        )
    }
}
