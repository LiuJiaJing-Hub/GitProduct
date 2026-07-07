package com.app.video.user.core.auth

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 认证模块的数据仓库。
 *
 * Fragment 只处理 UI，ViewModel 处理校验和状态，Repository 负责真正请求后端。
 */
class AuthRepository {
    private val gson = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun login(username: String, password: String): AuthUser {
        val rawJson = postJson(
            url = AuthConfig.LOGIN_URL,
            bodyObject = LoginRequest(username = username, password = password)
        )
        val responseType = object : TypeToken<ApiResponse<AuthUser>>() {}.type
        val apiResponse: ApiResponse<AuthUser> = gson.fromJson(rawJson, responseType)

        ensureSuccess(apiResponse.code, apiResponse.message, "登录失败")

        val user = apiResponse.data ?: throw IOException("登录失败：用户信息为空")
        if (user.realToken().isBlank()) {
            throw IOException("登录失败：token 为空")
        }
        return user
    }

    suspend fun register(username: String, password: String): AuthUser? {
        val rawJson = postJson(
            url = AuthConfig.REGISTER_URL,
            bodyObject = RegisterRequest(username = username, password = password)
        )
        val responseType = object : TypeToken<ApiResponse<AuthUser>>() {}.type
        val apiResponse: ApiResponse<AuthUser> = gson.fromJson(rawJson, responseType)

        ensureSuccess(apiResponse.code, apiResponse.message, "注册失败")
        return apiResponse.data
    }

    suspend fun resetPassword(username: String, password: String) {
        val rawJson = postJson(
            url = AuthConfig.SEND_FORGOT_PASSWORD_CODE_URL,
            bodyObject = ForgotPasswordRequest(username = username, password = password)
        )
        val responseType = object : TypeToken<ApiResponse<Any>>() {}.type
        val apiResponse: ApiResponse<Any> = gson.fromJson(rawJson, responseType)

        ensureSuccess(apiResponse.code, apiResponse.message, "重置密码失败")
    }

    private fun ensureSuccess(code: Int, message: String?, defaultMessage: String) {
        if (code != 0 && code != 200) {
            throw IOException(message ?: defaultMessage)
        }
    }

    private suspend fun postJson(url: String, bodyObject: Any): String =
        suspendCancellableCoroutine { continuation ->
            val requestBody = gson.toJson(bodyObject).toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val call = client.newCall(request)

            continuation.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (!continuation.isCancelled) {
                        continuation.resumeWithException(e)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use { resp ->
                        try {
                            val responseBody = resp.body?.string().orEmpty()
                            if (!resp.isSuccessful) {
                                throw IOException("HTTP ${resp.code}：$responseBody")
                            }
                            if (responseBody.isBlank()) {
                                throw IOException("服务器返回为空")
                            }
                            if (!continuation.isCancelled) {
                                continuation.resume(responseBody)
                            }
                        } catch (throwable: Throwable) {
                            if (!continuation.isCancelled) {
                                continuation.resumeWithException(throwable)
                            }
                        }
                    }
                }
            })
        }
}
