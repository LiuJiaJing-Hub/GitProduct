package com.hzmct.vodeodemo.service

import com.hzmct.vodeodemo.bean.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

/**
 * Socket 客户端连接状态
 */
data class ConnectionState(
    val isConnected: Boolean = false,
    val error: String? = null
)

/**
 * Socket 客户端单例管理器 —— 按需分页加载
 *
 * 通信协议（每行 JSON，\n 分隔）：
 *   客户端请求 → {"page": 0}
 *   服务端响应 → {"videos": [...], "isLastPage": false}
 */
object SocketClientManager {

    // 服务端地址：模拟器用 10.0.2.2 访问宿主机，真机改为实际 IP
    private const val HOST = "10.0.2.2"
    private const val PORT = 9999

    private var socket: Socket? = null
    private var writer: OutputStreamWriter? = null
    private var reader: BufferedReader? = null

    private val _connectionState = MutableStateFlow(ConnectionState())
    val connectionState = _connectionState.asStateFlow()

    /**
     * 建立 Socket 连接（仅连接，不拉取数据）
     */
    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        try {
            if (socket?.isConnected == true) return@withContext true

            socket = Socket(HOST, PORT)
            writer = OutputStreamWriter(socket!!.getOutputStream())
            reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))

            _connectionState.update { ConnectionState(isConnected = true) }
            true
        } catch (e: Exception) {
            _connectionState.update { ConnectionState(error = "连接失败: ${e.message}") }
            false
        }
    }

    /**
     * 向服务端请求指定页的视频数据
     * @param page 页码，从 0 开始
     * @return PageResponse 或 null（连接失败时）
     */
    suspend fun requestPage(page: Int): PageResponse? = withContext(Dispatchers.IO) {
        try {
            if (socket?.isConnected != true && !connect()) return@withContext null

            // 发送请求
            val requestJson = JSONObject().apply { put("page", page) }
            writer?.write(requestJson.toString() + "\n")
            writer?.flush()

            // 读取响应
            val responseLine = reader?.readLine() ?: return@withContext null
            val response = JSONObject(responseLine)

            val videosArray = response.getJSONArray("videos")
            val videos = mutableListOf<VideoItem>()
            for (i in 0 until videosArray.length()) {
                val obj = videosArray.getJSONObject(i)
                videos.add(
                    VideoItem(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        videoUrl = obj.getString("videoUrl")
                    )
                )
            }

            PageResponse(
                videos = videos,
                isLastPage = response.getBoolean("isLastPage")
            )
        } catch (e: Exception) {
            disconnect()
            _connectionState.update { ConnectionState(error = "请求失败: ${e.message}") }
            null
        }
    }

    /**
     * 断开连接并释放资源
     */
    fun disconnect() {
        try {
            writer?.close()
            reader?.close()
            socket?.close()
        } catch (_: Exception) {}
        writer = null
        reader = null
        socket = null
        _connectionState.update { ConnectionState() }
    }
}
