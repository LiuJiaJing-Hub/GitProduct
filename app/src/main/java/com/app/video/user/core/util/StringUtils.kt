package com.app.video.user.core.util

/**
 * 字符串处理工具类。
 * 提供常用的字符串判空、格式化和转换功能。
 */
object StringUtils {

    /**
     * 判断字符串是否为 null 或空字符串 ("")。
     *
     * @param str 要判断的字符串
     * @return 如果为 null 或空则返回 true，否则返回 false
     */
    fun isNullOrEmpty(str: String?): Boolean {
        return str.isNullOrEmpty()
    }

    /**
     * 判断字符串是否为 null 或仅包含空白字符。
     *
     * @param str 要判断的字符串
     * @return 如果为 null 或全为空白字符则返回 true，否则返回 false
     */
    fun isBlank(str: String?): Boolean {
        return str.isNullOrBlank()
    }

    /**
     * 将字符串的首字母转换为大写。
     *
     * @param str 要转换的字符串
     * @return 转换后的字符串
     */
    fun capitalize(str: String): String {
        if (str.isEmpty()) return str
        return str.substring(0, 1).uppercase() + str.substring(1)
    }

    /**
     * 反转字符串。
     *
     * @param str 要反转的字符串
     * @return 反转后的字符串
     */
    fun reverse(str: String): String {
        return str.reversed()
    }
}
