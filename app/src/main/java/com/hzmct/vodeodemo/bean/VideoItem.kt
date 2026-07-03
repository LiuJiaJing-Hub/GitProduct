package com.hzmct.vodeodemo.bean

// VideoItem.kt
data class VideoItem(
    val id: String,
    val title: String,
    val videoUrl: String,
    val coverUrl: String = "",  // 视频封面图 URL
)
