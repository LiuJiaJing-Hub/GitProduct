package com.app.video.user.core.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * JSON 序列化和反序列化工具类。
 * 基于 Gson 库提供对象的 JSON 转换功能。
 */
object JsonUtil {
    @PublishedApi
    internal val gson = Gson()

    /**
     * 将 Kotlin 对象转换为 JSON 字符串。
     *
     * @param src 要转换的 Kotlin 对象
     * @return 对应的 JSON 字符串
     */
    fun toJson(src: Any): String = gson.toJson(src)

    /**
     * 将 JSON 字符串反序列化为指定类型的 Kotlin 对象。
     *
     * @param T 目标对象的类型
     * @param json 要反序列化的 JSON 字符串
     * @return 反序列化后的 Kotlin 对象
     */
    inline fun <reified T> fromJson(json: String): T =
        gson.fromJson(json, object : TypeToken<T>() {}.type)
}
