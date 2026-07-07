package com.app.video.user.core.auth

/**
 * @liuJiaJing
 */
object AuthConfig {
    /**
     * Android 模拟器访问电脑本机服务时，用 10.0.2.2。
     *
     * 电脑浏览器访问：
     * http://localhost:8080
     *
     * Android 模拟器访问：
     * http://10.0.2.2:8080
     */
    const val BASE_URL = "http://10.0.2.2:8080"
    /**
     * 登录接口。
     *
     * 假设后端接口：
     * POST http://10.0.2.2:8080/auth/login
     */
    const val LOGIN_URL = "$BASE_URL/auth/login"

    /**
     * 注册接口。
     *
     * 假设后端接口：
     * POST http://10.0.2.2:8080/auth/register
     */
    const val REGISTER_URL = "$BASE_URL/auth/register"
    /**
     * 发送忘记密码验证码接口。
     *
     * 假设后端接口：
     * POST http://10.0.2.2:8080/auth/forgot-password/send-code
     */
    const val SEND_FORGOT_PASSWORD_CODE_URL =
        "$BASE_URL/auth/forgot-password/send-code"

    /**
     * 重置密码接口。
     *
     * 假设后端接口：
     * POST http://10.0.2.2:8080/auth/forgot-password/reset
     */
    const val RESET_PASSWORD_URL =
        "$BASE_URL/auth/forgot-password/reset"
}