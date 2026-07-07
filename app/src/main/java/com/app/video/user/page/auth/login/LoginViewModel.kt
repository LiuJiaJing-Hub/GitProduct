package com.app.video.user.page.auth.login

import com.app.video.user.AppVideoUserApplication
import com.app.video.user.domain.repository.AuthRepository
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 登录页面 UI 状态。
 *
 * isLoading = true 表示正在请求后端。
 */
data class LoginUiState(
    val isLoading: Boolean = false
)
/**
 * 登录页面一次性事件。
 *
 * ShowMessage：弹 Toast
 * LoginSuccess：登录成功，页面跳转 MainActivity
 */
sealed class LoginEvent {

    data class ShowMessage(
        val message: String
    ) : LoginEvent()

    object LoginSuccess : LoginEvent()
}

/**
 * 登录 ViewModel。
 *
 * 负责：
 * 1. 校验账号密码
 * 2. 调用登录接口
 * 3. 保存 token
 * 4. 通知 UI 登录结果
 */
class LoginViewModel : BaseViewModel() {
    private val repository = AuthRepository()
    private val tokenManager = TokenManager(
        AppVideoUserApplication.getAppContext()
    )
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun login(username: String, password: String) {
        if (_uiState.value.isLoading) {
            return
        }

        val realUsername = username.trim()
        val realPassword = password.trim()

        if (realUsername.isBlank()) {
            sendMessage("请输入账号")
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

        _uiState.update {
            it.copy(isLoading = true)
        }
        launch(
            onError = { throwable ->
                _uiState.update {
                    it.copy(isLoading = false)
                }

                _event.tryEmit(
                    LoginEvent.ShowMessage(
                        throwable.message ?: "登录失败，请稍后重试"
                    )
                )
            }
        ) {
            val user = repository.login(
                username = realUsername,
                password = realPassword
            )

            tokenManager.saveLoginInfo(
                token = user.realToken(),
                userId = user.realUserId(),
                username = user.realUsername()
            )

            _uiState.update {
                it.copy(isLoading = false)
            }

            _event.emit(LoginEvent.ShowMessage("登录成功"))
            _event.emit(LoginEvent.LoginSuccess)
        }
    }

    private fun sendMessage(message: String) {
        _event.tryEmit(
            LoginEvent.ShowMessage(message)
        )
    }
}
