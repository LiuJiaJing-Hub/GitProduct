package com.app.video.user.domain.repository


import com.app.video.user.core.auth.AuthConfig
import com.app.video.user.domain.model.ApiResponse
import com.app.video.user.domain.model.AuthUser
import com.app.video.user.domain.model.ForgotPasswordRequest
import com.app.video.user.domain.model.LoginRequest
import com.app.video.user.domain.model.RegisterRequest
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
    // JSON 请求体的媒体类型，表示 body 是 JSON 字符串，并且编码是 UTF-8
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
    /*
     * 把 OkHttp 的 callback 回调式网络请求
     *  包装成 Kotlin 协程里的 suspend 挂起函数
     */
    /*
     * suspendCancellableCoroutine 是 Kotlin 协程提供的一个函数，用于创建一个可取消的协程。
     */
    private suspend fun postJson(url: String, bodyObject: Any): String =
        suspendCancellableCoroutine { continuation ->
            val requestBody = gson.toJson(bodyObject).toRequestBody(jsonMediaType)
            //OkHttp 请求对象
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            //用 OkHttpClient 准备执行这个 request
            val call = client.newCall(request)

            //当协程取消时，取消这个 call
            continuation.invokeOnCancellation {
                call.cancel()
            }

            //用 OkHttp 执行这个请求，并使用 enqueue 方法异步回调结果
            call.enqueue(object : Callback {
                //请求失败，如果协程没有取消，则恢复协程并抛出异常
                override fun onFailure(call: Call, e: IOException) {
                    if (!continuation.isCancelled) {
                        continuation.resumeWithException(e)
                    }
                }

                //请求成功，如果协程没有取消，则恢复协程并返回响应体
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