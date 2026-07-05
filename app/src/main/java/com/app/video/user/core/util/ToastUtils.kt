package com.app.video.user.core.util

import android.widget.Toast
import com.app.video.user.AppVideoUserApplication

/**
 * Toast 工具类。
 * 提供显示短时间和长时间 Toast 的方法，避免 Toast 队列拥堵，并使用全局 Context 避免内存泄漏。
 */
object ToastUtils {

    private var toast: Toast? = null

    /**
     * 显示短时间的 Toast。
     *
     * @param message 要显示的文本内容
     */
    fun showShort(message: String) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(AppVideoUserApplication.getAppContext(), message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    /**
     * 显示长时间的 Toast。
     *
     * @param message 要显示的文本内容
     */
    fun showLong(message: String) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(AppVideoUserApplication.getAppContext(), message, Toast.LENGTH_LONG)
        toast?.show()
    }

    /**
     * 取消当前正在显示的 Toast。
     */
    fun cancelToast() {
        toast?.cancel()
        toast = null
    }
}
