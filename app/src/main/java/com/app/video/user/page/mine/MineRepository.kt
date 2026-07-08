package com.app.video.user.page.mine

import com.app.video.user.core.auth.TokenManager
import com.app.video.user.domain.model.MineActionItem
import com.app.video.user.domain.model.MineData
import com.app.video.user.domain.model.MineStat
import com.app.video.user.domain.model.MineUserProfile

class MineRepository(
    private val tokenManager: TokenManager
) {

    fun getMineData(): MineData {
        val username = tokenManager.getUsername().ifBlank { "已登录用户" }
        val userId = tokenManager.getUserId().ifBlank { "--" }
        return MineData(
            profile = MineUserProfile(
                username = username,
                userId = userId,
                isVip = false,
                vipText = "普通用户"
            ),
            stats = listOf(
                MineStat("观看", "12"),
                MineStat("收藏", "8"),
                MineStat("消息", "3")
            ),
            actions = listOf(
                MineActionItem("history", "观看历史", "继续上次的观看进度"),
                MineActionItem("favorite", "我的收藏", "管理收藏的视频内容"),
                MineActionItem("download", "我的下载", "离线缓存后续接入"),
                MineActionItem("message", "我的消息", "系统通知和互动消息"),
                MineActionItem("settings", "设置", "账号、安全和播放设置")
            )
        )
    }

    fun logout() {
        tokenManager.clearLoginInfo()
    }
}
