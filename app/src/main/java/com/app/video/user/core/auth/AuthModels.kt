package com.app.video.user.core.auth


/**
 * 通用后端响应结构。
 *
 * 假设后端返回：
 * {
 *   "code": 200,
 *   "message": "登录成功",
 *   "data": {}
 * }
 */
data class ApiResponse<T>(
    val code: Int = 0,
    val message: String? = null,
    val data: T? = null
)

/**
 * 登录请求体。
 *
 * 发送给后端：
 * {
 *   "username": "tom",
 *   "password": "123456"
 * }
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * 注册请求体。
 *
 * 发送给后端：
 * {
 *   "username": "tom",
 *   "password": "123456"
 * }
 */
data class RegisterRequest(
    val username: String,
    val password: String
)

/**
 * 忘记密码请求体。
 */
data class ForgotPasswordRequest(
    val username: String,
    val password: String
)

/**
 * 登录成功后后端返回的用户信息。
 *
 * 假设后端返回：
 * {
 *   "userId": "10001",
 *   "username": "tom",
 *   "token": "xxxxx.yyyyy.zzzzz"
 * }
 *
 * 这里 userId 和 id 都写了，是为了兼容不同后端字段。
 */
data class AuthUser(
    val userId: String? = null,
    val id: String? = null,
    val username: String? = null,
    val nickname: String? = null,
    val avatarUrl: String? = null,
    val token: String? = null,
    val isVip: Boolean = false,
    val vipExpireTime: String? = null
) {
    fun realUserId(): String {
        return userId.orEmpty().ifBlank { id.orEmpty() }
    }

    fun realUsername(): String {
        return username.orEmpty().ifBlank { nickname.orEmpty() }
    }

    fun realToken(): String {
        return token.orEmpty()
    }
}
