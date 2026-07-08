package com.app.video.user.domain.model

data class MineUserProfile(
    val username: String,
    val userId: String,
    val isVip: Boolean,
    val vipText: String
)

data class MineStat(
    val title: String,
    val value: String
)

data class MineActionItem(
    val id: String,
    val title: String,
    val description: String
)

data class MineData(
    val profile: MineUserProfile,
    val stats: List<MineStat>,
    val actions: List<MineActionItem>
)
