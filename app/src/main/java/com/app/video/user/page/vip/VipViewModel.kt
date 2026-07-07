package com.app.video.user.page.vip

import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VipUiState(
    val title: String = "会员",
    val description: String = "这里将展示会员身份、权益、套餐和 VIP 精选内容。",
    val status: String = "会员页容器已就绪。"
)

class VipViewModel : BaseViewModel() {
    private val _uiState = MutableStateFlow(VipUiState())
    val uiState = _uiState.asStateFlow()
}
