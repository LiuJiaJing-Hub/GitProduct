package com.app.video.user.page.shortdrama

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.ShortDramaCategory
import com.app.video.user.domain.model.ShortDramaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShortDramaUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<ShortDramaCategory> = emptyList(),
    val selectedCategoryId: String = "all",
    val dramas: List<ShortDramaItem> = emptyList()
)

class ShortDramaViewModel : BaseViewModel() {
    private val repository = ShortDramaRepository()
    private val _uiState = MutableStateFlow(ShortDramaUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
    private var allDramas: List<ShortDramaItem> = emptyList()

    init {
        loadShortDramas()
    }

    fun loadShortDramas() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "短剧数据加载失败"
                    )
                }
            }
        ) {
            val data = repository.getShortDramaData()
            allDramas = data.dramas
            _uiState.update {
                it.copy(
                    isLoading = false,
                    categories = data.categories,
                    dramas = filterDramas(it.selectedCategoryId)
                )
            }
        }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId,
                dramas = filterDramas(categoryId)
            )
        }
    }

    private fun filterDramas(categoryId: String): List<ShortDramaItem> {
        if (categoryId == "all") {
            return allDramas
        }
        return allDramas.filter { it.categoryId == categoryId }
    }
}
