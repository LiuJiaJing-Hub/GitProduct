package com.hzmct.vodeodemo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerState(
    val videoUrl: String = "",
    val isPlaying: Boolean = false
)

class PlayerViewModel : ViewModel() {
    /**
     * 状态管理
     * 目的：数据封装 —— 状态只能从内部（ViewModel）修改，外部（UI/Activity/Fragment）只能观察。
     */
    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()
    fun setVideo(url: String) {
        _state.value = PlayerState(videoUrl = url, isPlaying = true)
    }

    fun updatePlaying(isPlaying: Boolean) {
        _state.value = _state.value.copy(isPlaying = isPlaying)
    }
}