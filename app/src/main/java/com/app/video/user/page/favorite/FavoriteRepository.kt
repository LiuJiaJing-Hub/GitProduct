package com.app.video.user.page.favorite

import android.content.Context
import com.app.video.user.core.util.FavoriteStorageUtil
import com.app.video.user.domain.model.FavoriteVideoItem
import com.app.video.user.domain.repository.VideoRepository

/**
 * 新增收藏仓库，隔离收藏页对本地存储和视频数据源的访问。
 */
class FavoriteRepository(context: Context) {
    private val favoriteStorage = FavoriteStorageUtil(context)
    private val videoRepository = VideoRepository()

    suspend fun getFavorites(): List<FavoriteVideoItem> {
        return videoRepository.getFavoriteVideos(
            favoriteIds = favoriteStorage.getFavoriteIds(),
            addedTimeProvider = favoriteStorage::getFavoriteTime
        )
    }

    fun removeFavorite(videoId: String) {
        favoriteStorage.removeFavorite(videoId)
    }
}
