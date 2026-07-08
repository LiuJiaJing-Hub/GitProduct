package com.app.video.user.domain.model

data class VipUserStatus(
    val isVip: Boolean,
    val title: String,
    val expireTime: String?
)

data class VipBenefit(
    val id: String,
    val title: String,
    val description: String
)

data class VipPlan(
    val id: String,
    val title: String,
    val price: String,
    val originPrice: String,
    val description: String,
    val tag: String
)

data class VipRecommendItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val tag: String,
    val score: String
)

data class VipData(
    val userStatus: VipUserStatus,
    val benefits: List<VipBenefit>,
    val plans: List<VipPlan>,
    val recommends: List<VipRecommendItem>
)
