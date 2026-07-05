package com.app.video.user.core.util

import android.util.Log

/**
 * 日志工具类。
 * 封装了原生的 [Log] 方法，可以通过 [init] 方法控制是否输出日志，通常在 release 版本中关闭以提高性能和安全性。
 */
object LogUtils {

    /** 默认的全局日志 TAG */
    private const val TAG = "AppVideoUser"
    /** 控制是否打印日志的开关 */
    private var isDebug = true // Should be set based on build type

    /**
     * 初始化日志工具类。
     *
     * @param debugMode 是否为调试模式。true 打印日志，false 不打印日志
     */
    fun init(debugMode: Boolean) {
        isDebug = debugMode
    }

    /**
     * 打印 Verbose 级别的日志。
     *
     * @param msg 日志内容
     */
    fun v(msg: String) {
        if (isDebug) {
            Log.v(TAG, msg)
        }
    }

    /**
     * 打印 Debug 级别的日志。
     *
     * @param msg 日志内容
     */
    fun d(msg: String) {
        if (isDebug) {
            Log.d(TAG, msg)
        }
    }

    /**
     * 打印 Info 级别的日志。
     *
     * @param msg 日志内容
     */
    fun i(msg: String) {
        if (isDebug) {
            Log.i(TAG, msg)
        }
    }

    /**
     * 打印 Warn 级别的日志。
     *
     * @param msg 日志内容
     */
    fun w(msg: String) {
        if (isDebug) {
            Log.w(TAG, msg)
        }
    }

    /**
     * 打印 Error 级别的日志。
     *
     * @param msg 日志内容
     */
    fun e(msg: String) {
        if (isDebug) {
            Log.e(TAG, msg)
        }
    }

    /**
     * 打印带异常信息的 Error 级别日志。
     *
     * @param msg 日志内容
     * @param tr 异常对象，包含堆栈信息
     */
    fun e(msg: String, tr: Throwable) {
        if (isDebug) {
            Log.e(TAG, msg, tr)
        }
    }
}
