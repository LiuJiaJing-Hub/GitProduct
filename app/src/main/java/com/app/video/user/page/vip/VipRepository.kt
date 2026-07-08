package com.app.video.user.page.vip

import com.app.video.user.domain.model.VipBenefit
import com.app.video.user.domain.model.VipData
import com.app.video.user.domain.model.VipPlan
import com.app.video.user.domain.model.VipRecommendItem
import com.app.video.user.domain.model.VipUserStatus
import kotlinx.coroutines.delay

class VipRepository {

    suspend fun getVipData(): VipData {
        delay(180)
        return VipData(
            userStatus = VipUserStatus(
                isVip = false,
                title = "普通用户",
                expireTime = null
            ),
            benefits = listOf(
                VipBenefit("ad", "跳过广告", "看剧更连贯"),
                VipBenefit("hd", "高清画质", "优先体验高清内容"),
                VipBenefit("early", "抢先看", "热门剧集提前解锁"),
                VipBenefit("exclusive", "专属片库", "会员精选内容持续更新")
            ),
            plans = listOf(
                VipPlan("month", "月卡", "¥19", "¥25", "连续追剧更轻松", "推荐"),
                VipPlan("season", "季卡", "¥49", "¥68", "适合短期集中观看", "省 19"),
                VipPlan("year", "年卡", "¥168", "¥238", "全年畅看会员内容", "最划算")
            ),
            recommends = listOf(
                VipRecommendItem("vip_001", "暗夜档案", "会员抢先看 6 集", "VIP", "8.7"),
                VipRecommendItem("vip_002", "海岸线", "蓝光修复版", "会员", "8.3"),
                VipRecommendItem("vip_003", "云端少年", "高分动漫会员专享", "独享", "9.1")
            )
        )
    }
}
