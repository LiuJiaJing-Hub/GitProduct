package com.app.video.user.core.webSocket

/**
 * WebSocket 连接状态的数据类。
 * 包含连接状态、URL、最后一条消息和错误信息。
 *
 * @param status 当前的连接状态，默认为 [ConnectionStatus.IDLE]
 * @param url 当前连接的 WebSocket URL，默认为空字符串
 * @param lastMessage 最后接收到的消息，默认为空字符串
 * @param errorMessage 连接错误信息，如果没有错误则为 null
 */
data class WebSocketConnectionState(
    val status: ConnectionStatus = ConnectionStatus.IDLE,
    val url: String = "",
    val lastMessage: String = "",
    val errorMessage: String? = null
) {
    val isConnected: Boolean
        get() = status == ConnectionStatus.CONNECTED
}
