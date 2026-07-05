package com.app.video.user.core.http

import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * OkHttp 网络请求工具类。
 * 封装了常用的 GET、POST、单文件上传、多文件上传等方法。
 */
object OkHttpClientManager {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    /**
     * 发起 GET 请求。
     *
     * @param url 请求的 URL 地址
     * @param params 请求参数键值对
     * @param callback 网络请求回调接口
     * @return 返回 OkHttp 的 Call 对象，可用于取消请求
     */
    fun get(url: String, params: Map<String, String>, callback: Callback): Call {
        val httpUrlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
        params.forEach { (key, value) -> httpUrlBuilder.addQueryParameter(key, value) }

        val request = Request.Builder()
            .url(httpUrlBuilder.build())
            .get()
            .build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    /**
     * 发起 POST 表单请求。
     *
     * @param url 请求的 URL 地址
     * @param params 请求参数键值对（将作为表单数据提交）
     * @param callback 网络请求回调接口
     * @return 返回 OkHttp 的 Call 对象，可用于取消请求
     */
    fun post(url: String, params: Map<String, String>, callback: Callback): Call {
        val formBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        params.forEach { (key, value) -> formBodyBuilder.addFormDataPart(key, value) }

        val request = Request.Builder()
            .url(url)
            .post(formBodyBuilder.build())
            .build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    /**
     * 上传单文件以及附加参数。
     *
     * @param url 请求的 URL 地址
     * @param file 要上传的文件对象
     * @param fileParamName 文件在表单中的参数名
     * @param params 其他附加的表单参数
     * @param callback 网络请求回调接口
     * @return 返回 OkHttp 的 Call 对象，可用于取消请求
     */
    fun uploadFile(url: String, file: File, fileParamName: String, params: Map<String, String>, callback: Callback): Call {
        val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(fileParamName, file.name, requestBody)

        params.forEach { (key, value) -> multipartBodyBuilder.addFormDataPart(key, value) }

        val request = Request.Builder()
            .url(url)
            .post(multipartBodyBuilder.build())
            .build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    /**
     * 批量上传多文件以及附加参数。
     *
     * @param url 请求的 URL 地址
     * @param files 要上传的文件映射，键为表单参数名，值为对应的文件对象
     * @param params 其他附加的表单参数
     * @param callback 网络请求回调接口
     * @return 返回 OkHttp 的 Call 对象，可用于取消请求
     */
    fun uploadMultipleFiles(url: String, files: Map<String, File>, params: Map<String, String>, callback: Callback): Call {
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        files.forEach { (paramName, file) ->
            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            multipartBodyBuilder.addFormDataPart(paramName, file.name, requestBody)
        }

        params.forEach { (key, value) -> multipartBodyBuilder.addFormDataPart(key, value) }

        val request = Request.Builder()
            .url(url)
            .post(multipartBodyBuilder.build())
            .build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}
