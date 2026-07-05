package com.app.video.user.core.webSocket

/**
 * WebSocket 连接状态的枚举。
 * 定义了连接的各个阶段，如空闲、连接中、已连接、已断开和错误。
 */
enum class ConnectionStatus {
    IDLE,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    ERROR
}
