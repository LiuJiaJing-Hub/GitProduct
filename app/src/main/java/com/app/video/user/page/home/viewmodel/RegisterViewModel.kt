package com.app.video.user.page.home.viewmodel

import com.app.video.user.core.auth.AuthRepository
import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUiState(
    val isLoading: Boolean = false
)
sealed class RegisterEvent {

    data class ShowMessage(
        val message: String
    ) : RegisterEvent()

    object RegisterSuccess : RegisterEvent()
}

/**
 * 注册 ViewModel。
 *
 * 负责：
 * 1. 校验用户名
 * 2. 校验密码
 * 3. 校验两次密码是否一致
 * 4. 调用注册接口
 * 5. 通知 UI 注册结果
 */
class RegisterViewModel : BaseViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun register(username: String, password: String, confirmPassword: String) {
         if (_uiState.value.isLoading) {
             return
         }

         val realUsername = username.trim()
         val realPassword = password.trim()
         val realConfirmPassword = confirmPassword.trim()

         if (realUsername.isBlank()) {
             sendMessage("请输入用户名")
             return
         }

         if (realUsername.length < 3) {
             sendMessage("用户名不能少于 3 位")
             return
         }

         if (realPassword.isBlank()) {
             sendMessage("请输入密码")
             return
         }

         if (realPassword.length < 6) {
             sendMessage("密码不能少于 6 位")
             return
         }

         if (realConfirmPassword.isBlank()) {
             sendMessage("请再次输入密码")
             return
         }

         if (realPassword != realConfirmPassword) {
             sendMessage("两次输入的密码不一致")
             return
         }
         _uiState.update {
             it.copy(isLoading = true)
         }
         launch(
            onError = { throwable ->
                _event.tryEmit(RegisterEvent.ShowMessage(throwable.message ?: "注册失败，请稍后重试"))
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
         ) {
             repository.register(
                 username=realUsername,
                 password=realPassword
             )
             _uiState.update {
                 it.copy(isLoading = false)
             }

             _event.emit(RegisterEvent.ShowMessage("注册成功"))
             _event.emit(RegisterEvent.RegisterSuccess)
         }

    }
    private fun sendMessage(message: String) {
        _event.tryEmit(
            RegisterEvent.ShowMessage(message)
        )
    }
}
