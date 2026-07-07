package com.app.video.user.page.home

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.HomeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val homeData: HomeData? = null
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
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    homeData = homeData
                )
            }
        }
    }
}
