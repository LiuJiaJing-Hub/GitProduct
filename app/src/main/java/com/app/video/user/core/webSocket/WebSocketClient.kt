package com.app.video.user.core.webSocket

import com.app.video.user.core.constant.HttpConstants
import com.app.video.user.core.constant.WebSocketConstants
import com.app.video.user.core.util.JsonUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

/**
 * WebSocket 客户端，用于管理 WebSocket 连接、消息发送和接收。
 * 提供了连接、断开、发送消息、订阅和取消订阅主题的功能，并包含心跳机制。
 * 本身作为一个单例对象使用。
 */
object WebSocketClient {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val client = OkHttpClient.Builder()
        .connectTimeout(HttpConstants.connectTimeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .writeTimeout(HttpConstants.writeTimeoutSeconds, TimeUnit.SECONDS)
        .pingInterval(WebSocketConstants.heartbeatIntervalMillis, TimeUnit.MILLISECONDS)
        .build()

    private val _connectionState = MutableStateFlow(WebSocketConnectionState())
    val connectionState = _connectionState.asStateFlow()

    private val _incomingMessages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val incomingMessages = _incomingMessages.asSharedFlow()

    private var webSocket: WebSocket? = null
    private var heartbeatJob: Job? = null

    /**
     * 连接到指定的 WebSocket URL。
     * 如果已处于连接中或已连接状态，则不执行任何操作。
     *
     * @param url 目标 WebSocket 地址，默认为 [WebSocketConstants.defaultUrl]
     */
    fun connect(url: String = WebSocketConstants.defaultUrl) {
        if (_connectionState.value.status == ConnectionStatus.CONNECTING || _connectionState.value.isConnected) {
            return
        }
        _connectionState.value = WebSocketConnectionState(
            status = ConnectionStatus.CONNECTING,
            url = url
        )
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(
            request,
            AppWebSocketListener(
                onOpenAction = { socket, _ ->
                    webSocket = socket
                    _connectionState.value = WebSocketConnectionState(
                        status = ConnectionStatus.CONNECTED,
                        url = url
                    )
                    startHeartbeat()
                },
                onMessageAction = { message ->
                    _connectionState.value = _connectionState.value.copy(lastMessage = message, errorMessage = null)
                    scope.launch {
                        _incomingMessages.emit(message)
                    }
                },
                onClosingAction = { code, reason ->
                    stopHeartbeat()
                    _connectionState.value = _connectionState.value.copy(
                        status = ConnectionStatus.DISCONNECTED,
                        errorMessage = "closing: $code/$reason"
                    )
                },
                onClosedAction = { _, _ ->
                    stopHeartbeat()
                    _connectionState.value = _connectionState.value.copy(
                        status = ConnectionStatus.DISCONNECTED
                    )
                },
                onFailureAction = { throwable, _ ->
                    stopHeartbeat()
                    _connectionState.value = _connectionState.value.copy(
                        status = ConnectionStatus.ERROR,
                        errorMessage = throwable.message ?: "unknown websocket error"
                    )
                }
            )
        )
    }

    /**
     * 断开当前的 WebSocket 连接。
     * 同时停止心跳机制。
     */
    fun disconnect() {
        stopHeartbeat()
        webSocket?.close(
            WebSocketConstants.normalCloseCode,
            WebSocketConstants.normalCloseReason
        )
        webSocket = null
        _connectionState.value = _connectionState.value.copy(status = ConnectionStatus.DISCONNECTED)
    }

    /**
     * 通过 WebSocket 发送文本消息。
     * 消息内容会被封装成 JSON 格式。
     *
     * @param message 要发送的文本消息内容
     * @return 消息是否成功发送 (WebSocket 连接处于打开状态)
     */
    fun sendMessage(message: String): Boolean {
        if (!_connectionState.value.isConnected) {
            return false
        }
        val payload = JsonUtil.toJson(
            mapOf(
                "action" to WebSocketConstants.actionMessage,
                "body" to message
            )
        )
        return webSocket?.send(payload) == true
    }

    /**
     * 订阅指定主题的消息。
     *
     * @param topic 要订阅的主题名称
     * @return 订阅请求是否成功发送
     */
    fun subscribe(topic: String): Boolean {
        return sendAction(WebSocketConstants.actionSubscribe, topic)
    }

    /**
     * 取消订阅指定主题。
     *
     * @param topic 要取消订阅的主题名称
     * @return 取消订阅请求是否成功发送
     */
    fun unsubscribe(topic: String): Boolean {
        return sendAction(WebSocketConstants.actionUnsubscribe, topic)
    }

    /**
     * 发送 WebSocket 动作消息 (如订阅/取消订阅)。
     * 消息内容会被封装成 JSON 格式。
     *
     * @param action 动作类型 (例如 "subscribe", "unsubscribe")
     * @param topic 目标主题
     * @return 动作消息是否成功发送
     */
    private fun sendAction(action: String, topic: String): Boolean {
        if (!_connectionState.value.isConnected) {
            return false
        }
        val payload = JsonUtil.toJson(
            mapOf(
                "action" to action,
                "topic" to topic
            )
        )
        return webSocket?.send(payload) == true
    }

    /**
     * 启动心跳机制。
     * 定期发送心跳消息以保持 WebSocket 连接活跃。
     */
    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (_connectionState.value.isConnected) {
                delay(WebSocketConstants.heartbeatIntervalMillis)
                if (_connectionState.value.isConnected) {
                    webSocket?.send(
                        JsonUtil.toJson(
                            mapOf(
                                "action" to WebSocketConstants.actionHeartbeat,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                    )
                }
            }
        }
    }

    /**
     * 停止心跳机制。
     * 取消心跳协程。
     */
    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }
}
