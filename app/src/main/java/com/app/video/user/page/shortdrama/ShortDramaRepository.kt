package com.app.video.user.page.shortdrama

import com.app.video.user.domain.model.ShortDramaCategory
import com.app.video.user.domain.model.ShortDramaData
import com.app.video.user.domain.model.ShortDramaItem
import kotlinx.coroutines.delay

class ShortDramaRepository {

    suspend fun getShortDramaData(): ShortDramaData {
        delay(180)
        return ShortDramaData(
            categories = listOf(
                ShortDramaCategory("all", "全部"),
                ShortDramaCategory("sweet", "甜宠"),
                ShortDramaCategory("revenge", "逆袭"),
                ShortDramaCategory("city", "都市"),
                ShortDramaCategory("costume", "古风")
            ),
            dramas = listOf(
                ShortDramaItem("sd_001", "闪婚日记", "先婚后爱高甜来袭", "sweet", "甜宠", 80, 80, "320万", "98", "爆款", false),
                ShortDramaItem("sd_002", "逆袭合伙人", "小职员翻盘创业局", "revenge", "逆袭", 60, 36, "188万", "91", "更新", true),
                ShortDramaItem("sd_003", "晚风不迟到", "都市治愈轻喜剧", "city", "都市", 48, 48, "96万", "83", "完结", false),
                ShortDramaItem("sd_004", "将军的账本", "古风探案加轻喜", "costume", "古风", 72, 20, "121万", "88", "新剧", true),
                ShortDramaItem("sd_005", "重启人生计划", "低谷回归一路开挂", "revenge", "逆袭", 90, 90, "410万", "99", "高热", false),
                ShortDramaItem("sd_006", "隔壁老板太会撩", "职场欢喜冤家", "sweet", "甜宠", 66, 42, "152万", "87", "甜宠", false)
            )
        )
    }
}
