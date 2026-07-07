package com.app.video.user.page.auth.forgot

import com.app.video.user.domain.repository.AuthRepository
import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ForgotPasswordUiState(
    val isLoading: Boolean = false
)

sealed class ForgotPasswordEvent {
    data class ShowMessage(val message: String) : ForgotPasswordEvent()
    object ResetSuccess : ForgotPasswordEvent()
}

class ForgotPasswordViewModel : BaseViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ForgotPasswordEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun resetPassword(username: String, password: String, confirmPassword: String) {
        if (_uiState.value.isLoading) {
            return
        }

        val realUsername = username.trim()
        val realPassword = password.trim()
        val realConfirmPassword = confirmPassword.trim()

        if (realUsername.isBlank()) {
            sendMessage("请输入账号")
            return
        }
        if (realPassword.isBlank()) {
            sendMessage("请输入新密码")
            return
        }
        if (realPassword.length < 6) {
            sendMessage("密码不能少于 6 位")
            return
        }
        if (realConfirmPassword.isBlank()) {
            sendMessage("请再次输入新密码")
            return
        }
        if (realPassword != realConfirmPassword) {
            sendMessage("两次输入的密码不一致")
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        launch(
            onError = { throwable ->
                _uiState.update { it.copy(isLoading = false) }
                _event.tryEmit(
                    ForgotPasswordEvent.ShowMessage(
                        throwable.message ?: "重置密码失败，请稍后重试"
                    )
                )
            }
        ) {
            repository.resetPassword(realUsername, realPassword)
            _uiState.update { it.copy(isLoading = false) }
            _event.emit(ForgotPasswordEvent.ShowMessage("密码已重置，请重新登录"))
            _event.emit(ForgotPasswordEvent.ResetSuccess)
        }
    }

    private fun sendMessage(message: String) {
        _event.tryEmit(ForgotPasswordEvent.ShowMessage(message))
    }
}
