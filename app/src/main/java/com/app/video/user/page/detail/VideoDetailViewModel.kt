package com.app.video.user.page.detail

import com.app.video.user.AppVideoUserApplication
import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.core.util.FavoriteStorageUtil
import com.app.video.user.domain.model.EpisodeItem
import com.app.video.user.domain.model.VideoDetail
import com.app.video.user.domain.repository.VideoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VideoDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val detail: VideoDetail? = null,
    val selectedEpisodeId: String? = null
)
/*
  *sealed
 */
sealed class VideoDetailEvent {
    data class NavigatePlayer(
        val videoId: String,
        val episodeId: String,
        val playUrl: String,
        val title: String
    ) : VideoDetailEvent()

    data class ShowMessage(val message: String) : VideoDetailEvent()
}

/**
 * 视频详情页 ViewModel，负责加载详情、选集选择、收藏和播放跳转事件。
 */
class VideoDetailViewModel : BaseViewModel() {
    private val repository = VideoRepository()
    private val favoriteStorage = FavoriteStorageUtil(AppVideoUserApplication.getAppContext())
    /*
        创建可变的状态流，对外暴露只读接口，防止外部直接修改状态
     */
    private val _uiState = MutableStateFlow(VideoDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<VideoDetailEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun loadVideo(videoId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "视频详情加载失败"
                    )
                }
            }
        ) {
            val detail = repository.getVideoDetail(
                videoId = videoId,
                isFavorite = favoriteStorage.isFavorite(videoId)
            )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    detail = detail,
                    selectedEpisodeId = detail.episodes.firstOrNull()?.id
                )
            }
        }
    }

    fun selectEpisode(episodeId: String) {
        _uiState.update { it.copy(selectedEpisodeId = episodeId) }
    }

    fun toggleFavorite() {
        val detail = _uiState.value.detail ?: return
        val isFavorite = favoriteStorage.toggleFavorite(detail.video.id)
        _uiState.update {
            it.copy(detail = detail.copy(isFavorite = isFavorite))
        }
        _event.tryEmit(
            VideoDetailEvent.ShowMessage(if (isFavorite) "已加入收藏" else "已取消收藏")
        )
    }

    fun playSelectedEpisode() {
        val detail = _uiState.value.detail ?: return
        val episode = findSelectedEpisode(detail) ?: return
        _event.tryEmit(
            VideoDetailEvent.NavigatePlayer(
                videoId = detail.video.id,
                episodeId = episode.id,
                playUrl = episode.playUrl,
                title = "${detail.video.title} ${episode.title}"
            )
        )
    }

    private fun findSelectedEpisode(detail: VideoDetail): EpisodeItem? {
        return detail.episodes.firstOrNull { it.id == _uiState.value.selectedEpisodeId }
            ?: detail.episodes.firstOrNull()
    }
}
