package com.app.video.user.page.player

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlayerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val videoId: String = "",
    val episodeId: String = "",
    val title: String = "",
    val playUrl: String = ""
)

/**
 * 新增播放页 ViewModel，负责整理播放入参并在缺少 playUrl 时兜底查询选集数据。
 */
class PlayerViewModel : BaseViewModel() {
    private val repository = VideoRepository()
    private val _uiState = MutableStateFlow(PlayerUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun loadPlayerData(
        videoId: String,
        episodeId: String?,
        playUrl: String?,
        title: String?
    ) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "播放数据加载失败"
                    )
                }
            }
        ) {
            val episode = repository.getEpisode(videoId, episodeId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    videoId = videoId,
                    episodeId = episode.id,
                    title = title.orEmpty().ifBlank { episode.title },
                    playUrl = playUrl.orEmpty().ifBlank { episode.playUrl }
                )
            }
        }
    }
}
