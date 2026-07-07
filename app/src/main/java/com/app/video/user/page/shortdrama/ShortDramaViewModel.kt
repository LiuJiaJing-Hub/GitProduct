package com.app.video.user.page.shortdrama

import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ShortDramaUiState(
    val title: String = "短剧",
    val description: String = "这里将承载短剧列表，后续可升级为上下滑短视频式播放体验。",
    val status: String = "短剧页容器已就绪。"
)

class ShortDramaViewModel : BaseViewModel() {
    private val _uiState = MutableStateFlow(ShortDramaUiState())
    val uiState = _uiState.asStateFlow()
}
