package com.app.video.user.page.mine

import com.app.video.user.AppVideoUserApplication
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.core.base.BaseViewModel
import com.app.video.user.domain.model.MineData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class MineUiState(
    val mineData: MineData
)

sealed class MineEvent {
    object LogoutSuccess : MineEvent()
    object NavigateFavorite : MineEvent()
    data class ShowMessage(val message: String) : MineEvent()
}

class MineViewModel : BaseViewModel() {
    private val tokenManager = TokenManager(AppVideoUserApplication.getAppContext())
    private val repository = MineRepository(tokenManager)
    private val _uiState = MutableStateFlow(MineUiState(repository.getMineData()))
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MineEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun onActionClick(actionId: String) {
        when (actionId) {
            "favorite" -> _event.tryEmit(MineEvent.NavigateFavorite)
            "history" -> _event.tryEmit(MineEvent.ShowMessage("观看历史后续接入"))
            "download" -> _event.tryEmit(MineEvent.ShowMessage("离线缓存后续接入"))
            "message" -> _event.tryEmit(MineEvent.ShowMessage("消息中心后续接入"))
            "settings" -> _event.tryEmit(MineEvent.ShowMessage("设置页后续接入"))
            else -> _event.tryEmit(MineEvent.ShowMessage("功能后续接入"))
        }
    }

    fun logout() {
        repository.logout()
        _event.tryEmit(MineEvent.LogoutSuccess)
    }
}
