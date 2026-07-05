package com.app.video.user.core.util

import java.io.File

/**
 * 文件操作工具类。
 * 提供文件的读取和写入功能。
 */
object FileUtil {

    /**
     * 读取文件的文本内容。
     * 如果文件不存在，则返回空字符串。
     *
     * @param file 要读取的文件对象
     * @return 文件的文本内容，如果文件不存在则返回空字符串
     */
    fun readText(file: File): String = if (file.exists()) file.readText() else ""

    /**
     * 将文本内容写入文件。
     * 如果文件或其父目录不存在，则会创建。
     *
     * @param file 要写入的文件对象
     * @param text 要写入的文本内容
     */
    fun writeText(file: File, text: String) {
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        file.writeText(text)
    }
}
