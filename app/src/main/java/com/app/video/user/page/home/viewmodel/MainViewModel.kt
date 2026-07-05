package com.app.video.user.page.home.viewmodel

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.core.constant.WebSocketConstants
import com.app.video.user.core.service.NetService
import com.app.video.user.core.util.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 首页的 UI 状态数据类。
 * 包含当前连接状态文本、消息日志和当前订阅的主题。
 */
data class MainUiState(
    val connectionText: String = "未连接",
    val messageLog: String = "等待 WebSocket 消息...",
    val topic: String = WebSocketConstants.defaultTopic
)

/**
 * 首页对应的 ViewModel。
 * 负责处理首页相关的业务逻辑，与 [NetService] 交互，并将结果作为状态暴露给 UI 层。
 */
class MainViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    /** 向外暴露的只读 UI 状态流 */
    val uiState = _uiState.asStateFlow()

    init {

    }


    /**
     * 向界面日志中追加新的一行内容。
     *
     * @param content 追加的内容
     */
    private fun appendLog(content: String) {
        _uiState.update { current ->
            val nextLine = "${DateUtil.now()}  $content"
            val merged = if (current.messageLog.isBlank()) nextLine else "${current.messageLog}\n$nextLine"
            current.copy(messageLog = merged)
        }
    }
}