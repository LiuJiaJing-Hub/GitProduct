package com.app.video.user.core.util

import com.app.video.user.core.constant.AppConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 日期时间处理工具类。
 * 提供时间戳格式化和获取当前时间字符串等便捷方法。
 */
object DateUtil {

    /**
     * 将给定的时间戳格式化为指定的日期字符串格式。
     *
     * @param timestamp 毫秒级时间戳
     * @param pattern   时间格式模板，默认为 [AppConstants.defaultDatePattern]
     * @return 格式化后的时间字符串
     */
    fun format(timestamp: Long, pattern: String = AppConstants.defaultDatePattern): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    /**
     * 获取当前系统时间，并按指定格式格式化。
     *
     * @param pattern 时间格式模板，默认为 [AppConstants.defaultDatePattern]
     * @return 格式化后的当前时间字符串
     */
    fun now(pattern: String = AppConstants.defaultDatePattern): String = format(
        timestamp = System.currentTimeMillis(),
        pattern = pattern
    )
}
