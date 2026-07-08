package com.app.video.user.page.home

import android.util.Log
import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.HomeData
import com.app.video.user.domain.model.HomeSection
import com.app.video.user.domain.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val homeData: HomeData? = null,
    val selectedChannelId: String = "recommend",
    val displayContinueWatching: List<VideoItem> = emptyList(),
    val displaySections: List<HomeSection> = emptyList()
)

class HomeViewModel : BaseViewModel() {
    private val repository = HomeRepository()
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "首页数据加载失败"
                    )
                }
            }
        ) {
            val homeData = repository.getHomeData()
            val filteredContent = filterHomeContent(homeData, _uiState.value.selectedChannelId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    homeData = homeData,
                    displayContinueWatching = filteredContent.first,
                    displaySections = filteredContent.second
                )
            }
        }
    }

    fun selectHomeChannel(channelId: String) {
        val homeData = _uiState.value.homeData ?: return
        Log.d("HomeViewModel", "Selected channel: $channelId")
        val filteredContent = filterHomeContent(homeData, channelId)
        _uiState.update {
            it.copy(
                selectedChannelId = channelId,
                displayContinueWatching = filteredContent.first,
                displaySections = filteredContent.second
            )
        }
    }

    private fun filterHomeContent(
        homeData: HomeData,
        channelId: String
    ): Pair<List<VideoItem>, List<HomeSection>> {
        if (channelId == "recommend") {
            return homeData.continueWatching to homeData.sections
        }

        val continueWatching = homeData.continueWatching.filter { video ->
            video.categoryId == channelId
        }
        val sections = filterSections(homeData, channelId)
        return continueWatching to sections
    }

    private fun filterSections(homeData: HomeData, channelId: String): List<HomeSection> {
        return homeData.sections.mapNotNull { section ->
            val videos = section.videos.filter { video -> video.categoryId == channelId }
            if (videos.isEmpty()) null else section.copy(videos = videos)
        }
    }
}
