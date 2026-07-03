package com.hzmct.vodeodemo

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

/**
 * ExoPlayer 单例管理器
 * 防止 Fragment 销毁时视频被重新创建
 */
object ExoPlayerManager {
    private var player: ExoPlayer? = null

    fun get(context: Context): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context.applicationContext).build()
        }
        return player!!
    }

    fun release() {
        player?.release()
        player = null
    }
}
