package com.app.video.user.core.constant

/**
 * 定义 HTTP 相关的常量，包括基础 URL、连接超时、读取超时和写入超时时间。
 */
object HttpConstants {
    /** API 基础 URL */
    const val baseUrl = "https://api.example.com/"
    /** 连接超时时间，单位秒 */
    const val connectTimeoutSeconds = 30L
    /** 读取超时时间，单位秒 */
    const val readTimeoutSeconds = 30L
    /** 写入超时时间，单位秒 */
    const val writeTimeoutSeconds = 30L
}
