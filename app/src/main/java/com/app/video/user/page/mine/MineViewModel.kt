package com.app.video.user.page.mine

import com.app.video.user.AppVideoUserApplication
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MineUiState(
    val title: String = "我的",
    val description: String = "这里将展示用户信息、观看历史、收藏、下载、消息和设置入口。",
    val status: String
)

class MineViewModel : BaseViewModel() {
    private val tokenManager = TokenManager(AppVideoUserApplication.getAppContext())
    private val _uiState = MutableStateFlow(
        MineUiState(
            status = "当前用户：${tokenManager.getUsername().ifBlank { "已登录用户" }}"
        )
    )
    val uiState = _uiState.asStateFlow()
}
