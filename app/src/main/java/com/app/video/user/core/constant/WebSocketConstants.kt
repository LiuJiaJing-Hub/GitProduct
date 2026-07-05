package com.app.video.user.core.constant

/**
 * 定义 WebSocket 相关的常量，包括默认 URL、主题、操作类型、心跳间隔和关闭码。
 */
object WebSocketConstants {
    /** 默认 WebSocket 服务器地址 */
    const val defaultUrl = "ws://10.0.2.2:8080/ws"
    /** 默认订阅主题 */
    const val defaultTopic = "video-room"
    /** 订阅操作的动作字符串 */
    const val actionSubscribe = "subscribe"
    /** 取消订阅操作的动作字符串 */
    const val actionUnsubscribe = "unsubscribe"
    /** 心跳消息的动作字符串 */
    const val actionHeartbeat = "heartbeat"
    /** 普通消息的动作字符串 */
    const val actionMessage = "message"
    /** 心跳间隔时间，单位毫秒 */
    const val heartbeatIntervalMillis = 30_000L
    /** 正常关闭 WebSocket 的状态码 */
    const val normalCloseCode = 1000
    /** 正常关闭 WebSocket 的原因 */
    const val normalCloseReason = "manual disconnect"
}
