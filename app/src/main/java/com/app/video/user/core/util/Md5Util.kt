package com.app.video.user.core.util

import java.security.MessageDigest

/**
 * MD5 加密工具类。
 * 提供 MD5 字符串加密功能。
 */
object Md5Util {

    /**
     * 对字符串进行 MD5 加密。
     *
     * @param content 要加密的字符串
     * @return 加密后的 MD5 字符串
     */
    fun md5(content: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(content.toByteArray())
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }
}
