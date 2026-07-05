package com.app.video.user.core.util

import com.app.video.user.AppVideoUserApplication
import java.io.File
import java.io.IOException

/**
 * 文件操作工具类。
 * 提供字符串与文件之间的读写操作，以及获取应用私有目录路径的方法。
 */
object FileUtils {

    /**
     * 将字符串写入到指定文件中。
     *
     * @param filePath 目标文件的绝对路径
     * @param content  要写入的字符串内容
     * @return 写入是否成功
     */
    fun writeStringToFile(filePath: String, content: String): Boolean {
        return try {
            File(filePath).writeText(content)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 从指定文件中读取字符串内容。
     *
     * @param filePath 目标文件的绝对路径
     * @return 读取到的字符串内容，如果发生错误则返回 null
     */
    fun readStringFromFile(filePath: String): String? {
        return try {
            File(filePath).readText()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 删除指定文件。
     *
     * @param filePath 要删除文件的绝对路径
     * @return 删除是否成功
     */
    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: SecurityException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 检查文件是否存在。
     *
     * @param filePath 文件的绝对路径
     * @return 存在返回 true，否则返回 false
     */
    fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists()
    }

    /**
     * 获取应用的私有缓存目录路径 (cacheDir)。
     * 内部使用全局的 Application Context 获取，无需额外传递 Context。
     *
     * @return 缓存目录的绝对路径
     */
    fun getCacheDir(): String {
        return AppVideoUserApplication.getAppContext().cacheDir.absolutePath
    }

    /**
     * 获取应用的私有文件目录路径 (filesDir)。
     * 内部使用全局的 Application Context 获取，无需额外传递 Context。
     *
     * @return 文件目录的绝对路径
     */
    fun getFilesDir(): String {
        return AppVideoUserApplication.getAppContext().filesDir.absolutePath
    }
}
