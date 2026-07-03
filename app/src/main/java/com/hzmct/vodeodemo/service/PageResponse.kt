package com.hzmct.vodeodemo.service

import com.hzmct.vodeodemo.bean.VideoItem

/**
 * Socket 分页响应
 */
data class PageResponse(
    val videos: List<VideoItem>,
    val isLastPage: Boolean
)