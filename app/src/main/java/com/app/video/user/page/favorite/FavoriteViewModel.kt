package com.app.video.user.page.favorite

import com.app.video.user.AppVideoUserApplication
import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.FavoriteVideoItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val favorites: List<FavoriteVideoItem> = emptyList()
)

sealed class FavoriteEvent {
    data class NavigateDetail(val videoId: String) : FavoriteEvent()
    data class ShowMessage(val message: String) : FavoriteEvent()
}

/**
 * 新增收藏页 ViewModel，负责收藏列表加载、删除和详情跳转事件。
 */
class FavoriteViewModel : BaseViewModel() {
    private val repository = FavoriteRepository(AppVideoUserApplication.getAppContext())
    private val _uiState = MutableStateFlow(FavoriteUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FavoriteEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "收藏列表加载失败"
                    )
                }
            }
        ) {
            val favorites = repository.getFavorites()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    favorites = favorites
                )
            }
        }
    }

    fun openDetail(videoId: String) {
        _event.tryEmit(FavoriteEvent.NavigateDetail(videoId))
    }

    fun removeFavorite(videoId: String) {
        repository.removeFavorite(videoId)
        _event.tryEmit(FavoriteEvent.ShowMessage("已移出收藏"))
        loadFavorites()
    }
}
