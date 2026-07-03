package com.hzmct.vodeodemo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 登录页 UI 状态
 * @param username          用户名输入
 * @param password          密码输入
 * @param isPasswordVisible 密码是否明文显示
 * @param errorMessage      错误提示（非 null 时弹出 Toast）
 * @param navigateToHome    登录成功 → 导航到首页
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String? = null,
    val navigateToHome: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(text: String) {
        _uiState.update { it.copy(username = text.trim()) }
    }

    fun onPasswordChanged(text: String) {
        _uiState.update { it.copy(password = text.trim()) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val state = _uiState.value

        if (state.username.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "用户名不能为空") }
            return
        }
        if (state.password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "密码不能为空") }
            return
        }

        if (state.username == "123" && state.password == "123") {
            _uiState.update { it.copy(navigateToHome = true) }
        } else {
            _uiState.update { it.copy(errorMessage = "用户名或密码输入不正确") }
        }
    }

    /** Fragment 消费完 errorMessage 后调用，避免重复 Toast */
    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /** Fragment 导航完成后调用，重置标记 */
    fun onNavigated() {
        _uiState.update { it.copy(navigateToHome = false) }
    }
}
