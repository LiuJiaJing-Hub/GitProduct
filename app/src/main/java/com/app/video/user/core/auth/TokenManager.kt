package com.app.video.user.core.auth

import android.content.Context

/**
 * 登录态管理类。
 *
 * 负责保存：
 * 1. token
 * 2. userId
 * 3. username
 * 4. 是否已登录
 *
 * 用 SharedPreferences 足够。
 */
class TokenManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "auth_info",
        Context.MODE_PRIVATE
    )
    fun saveLoginInfo(
        token: String,
        userId: String,
        username: String
    ) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putBoolean(KEY_IS_LOGIN, true)
            .apply()
    }
    fun getToken(): String {
        return sharedPreferences.getString(KEY_TOKEN, "").orEmpty()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "").orEmpty()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGIN, false) &&
                getToken().isNotBlank()
    }

    fun clearLoginInfo() {
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_USERNAME)
            .putBoolean(KEY_IS_LOGIN, false)
            .apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGIN = "is_login"
    }
}