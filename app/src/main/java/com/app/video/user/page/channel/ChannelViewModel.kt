package com.app.video.user.page.channel

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.ChannelData
import com.app.video.user.domain.model.ChannelSelection
import com.app.video.user.domain.model.ChannelVideoItem
import com.app.video.user.domain.model.FilterOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChannelUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<FilterOption> = emptyList(),
    val areas: List<FilterOption> = emptyList(),
    val years: List<FilterOption> = emptyList(),
    val sorts: List<FilterOption> = emptyList(),
    val selection: ChannelSelection = ChannelSelection(),
    val videos: List<ChannelVideoItem> = emptyList()
)

class ChannelViewModel : BaseViewModel() {
    private val repository = ChannelRepository()
    private val _uiState = MutableStateFlow(ChannelUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var channelData: ChannelData? = null

    init {
        loadChannelData()
    }

    fun loadChannelData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "频道数据加载失败"
                    )
                }
            }
        ) {
            val data = repository.getChannelData()
            channelData = data
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    categories = data.categories,
                    areas = data.areas,
                    years = data.years,
                    sorts = data.sorts,
                    videos = filterVideos(data, it.selection)
                )
            }
        }
    }

    fun selectCategory(categoryId: String) {
        updateSelection { it.copy(categoryId = categoryId) }
    }

    fun selectArea(areaId: String) {
        updateSelection { it.copy(areaId = areaId) }
    }

    fun selectYear(yearId: String) {
        updateSelection { it.copy(yearId = yearId) }
    }

    fun selectSort(sortId: String) {
        updateSelection { it.copy(sortId = sortId) }
    }

    private fun updateSelection(reducer: (ChannelSelection) -> ChannelSelection) {
        val data = channelData ?: return
        _uiState.update { current ->
            val nextSelection = reducer(current.selection)
            current.copy(
                selection = nextSelection,
                videos = filterVideos(data, nextSelection)
            )
        }
    }

    private fun filterVideos(data: ChannelData, selection: ChannelSelection): List<ChannelVideoItem> {
        return data.videos
            .filter { selection.categoryId == "all" || it.categoryId == selection.categoryId }
            .filter { selection.areaId == "all" || it.areaId == selection.areaId }
            .filter { selection.yearId == "all" || it.yearId == selection.yearId }
            .let { videos ->
                when (selection.sortId) {
                    "new" -> videos.sortedByDescending { it.publishTime }
                    "score" -> videos.sortedByDescending { it.ratingScore }
                    else -> videos.sortedByDescending { it.hotScore }
                }
            }
    }
}
