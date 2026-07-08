package com.app.video.user.page.vip

import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.VipData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VipUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val vipData: VipData? = null,
    val selectedPlanId: String = "month"
)

sealed class VipEvent {
    data class ShowMessage(val message: String) : VipEvent()
}

class VipViewModel : BaseViewModel() {
    private val repository = VipRepository()
    private val _uiState = MutableStateFlow(VipUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<VipEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    init {
        loadVipData()
    }

    fun loadVipData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "会员数据加载失败"
                    )
                }
            }
        ) {
            val data = repository.getVipData()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    vipData = data,
                    selectedPlanId = data.plans.firstOrNull()?.id ?: "month"
                )
            }
        }
    }

    fun selectPlan(planId: String) {
        _uiState.update { it.copy(selectedPlanId = planId) }
    }

    fun openSelectedPlan() {
        val plan = _uiState.value.vipData?.plans?.firstOrNull {
            it.id == _uiState.value.selectedPlanId
        }
        _event.tryEmit(VipEvent.ShowMessage("${plan?.title ?: "会员"}支付功能后续接入"))
    }
}
