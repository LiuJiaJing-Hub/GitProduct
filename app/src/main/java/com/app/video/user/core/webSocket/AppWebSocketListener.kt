package com.app.video.user.core.webSocket

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * 自定义的 WebSocket 监听器，继承自 [WebSocketListener]。
 * 通过回调函数处理 WebSocket 的各种生命周期事件，如连接打开、接收消息、连接关闭等。
 *
 * @param onOpenAction 当 WebSocket 连接成功打开时触发的回调
 * @param onMessageAction 当接收到 WebSocket 文本消息时触发的回调
 * @param onClosingAction 当远程端点指示不再发送消息时触发的回调
 * @param onClosedAction 当 WebSocket 连接已完全关闭时触发的回调
 * @param onFailureAction 当 WebSocket 连接发生错误时触发的回调
 */
class AppWebSocketListener(
    private val onOpenAction: (WebSocket, Response) -> Unit,
    private val onMessageAction: (String) -> Unit,
    private val onClosingAction: (Int, String) -> Unit,
    private val onClosedAction: (Int, String) -> Unit,
    private val onFailureAction: (Throwable, Response?) -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        onOpenAction(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageAction(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        onClosingAction(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onClosedAction(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onFailureAction(t, response)
    }
}
