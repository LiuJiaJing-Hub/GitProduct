package com.app.video.user.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.app.video.user.R
import com.app.video.user.core.constant.AppConstants
import com.app.video.user.core.constant.WebSocketConstants
import com.app.video.user.core.webSocket.WebSocketClient

/**
 * 前台网络服务，用于保持应用在后台的存活，并管理 WebSocket 连接的生命周期。
 * 业务层可直接通过 [NetService.webSocketClient] 进行订阅、发送消息等操作。
 */
class NetService : Service() {

    companion object {
        /**
         * 启动 NetService 前台服务。
         */
        fun startService(context: Context) {
            val intent = Intent(context, NetService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * 停止 NetService。
         */
        fun stopService(context: Context) {
            val intent = Intent(context, NetService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(AppConstants.notificationId, getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 服务启动或被杀重启时，确保 WebSocket 连接活跃
        if (!WebSocketClient.connectionState.value.isConnected) {
            WebSocketClient.connect(WebSocketConstants.defaultUrl)
        }
        return START_STICKY // 被杀后自动重启
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketClient.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // 这是一个 Started Service，不支持绑定
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                AppConstants.notificationChannelId,
                "网络服务通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun getNotification(): Notification {
        return NotificationCompat.Builder(this, AppConstants.notificationChannelId)
            .setContentTitle("网络服务运行中")
            .setContentText("保持 WebSocket 连接活跃")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 默认占位图标
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }
}