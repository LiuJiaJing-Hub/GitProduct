package com.app.video.user.core.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 新增收藏本地存储工具。
 *
 * V1 阶段先使用 SharedPreferences 保存收藏视频 id 和收藏时间，
 * 后续接入后端时可以在 Repository 内替换为接口请求。
 */
class FavoriteStorageUtil(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isFavorite(videoId: String): Boolean {
        return getFavoriteIds().contains(videoId)
    }
    /*
     * 判断是否收藏，如果已经收藏就取消，没有就收藏
     */
    fun toggleFavorite(videoId: String): Boolean {
        return if (isFavorite(videoId)) {
            removeFavorite(videoId)
            false
        } else {
            addFavorite(videoId)
            true
        }
    }

    fun addFavorite(videoId: String) {
        val ids = getFavoriteIds().toMutableSet()
        ids.add(videoId)
        preferences.edit()
            .putStringSet(KEY_IDS, ids)
            .putString(timeKey(videoId), nowText())
            .apply()
    }

    fun removeFavorite(videoId: String) {
        val ids = getFavoriteIds().toMutableSet()
        ids.remove(videoId)
        preferences.edit()
            .putStringSet(KEY_IDS, ids)
            .remove(timeKey(videoId))
            .apply()
    }

    fun getFavoriteIds(): List<String> {
        return preferences.getStringSet(KEY_IDS, mutableSetOf()).orEmpty().toList()
    }

    fun getFavoriteTime(videoId: String): String {
        return preferences.getString(timeKey(videoId), "").orEmpty()
    }

    private fun nowText(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }

    private fun timeKey(videoId: String): String = "$KEY_TIME_PREFIX$videoId"

    companion object {
        private const val PREF_NAME = "favorite_info"
        private const val KEY_IDS = "favorite_ids"
        private const val KEY_TIME_PREFIX = "favorite_time_"
    }
}
