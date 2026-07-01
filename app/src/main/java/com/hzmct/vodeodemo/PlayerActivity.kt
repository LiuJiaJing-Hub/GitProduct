package com.hzmct.vodeodemo

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


class PlayerActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        // 获取传过来的数据
        val videoUrl = intent.getStringExtra("video_url") ?: return
        val videoTitle = intent.getStringExtra("video_title") ?: ""
        findViewById<TextView>(R.id.tvTitle).text=videoTitle
        playerView = findViewById(R.id.playerView)
        initPlayer(videoUrl)
        val orientation = screenLandPort(this)
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
    fun screenLandPort(activity: Activity): Int {
        val windowManager = activity.getWindowManager()
        val display = windowManager.getDefaultDisplay()
        val screenWidth = display.getWidth()
        val screenHeight = display.getHeight()
        //return  0:横屏；1:竖屏
        return if (screenWidth < screenHeight) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun initPlayer(url: String) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        player?.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
        player?.prepare()
        player?.play()
    }
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}