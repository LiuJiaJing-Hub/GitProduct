package com.app.video.user.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel 的基础类。
 * 提供了一些基础的封装方法，例如协程的启动与统一异常捕获。
 */
open class BaseViewModel : ViewModel() {

    /**
     * 在 [viewModelScope] 中启动一个协程，并自带异常捕获处理。
     *
     * @param onError 捕获到异常时的回调，默认不处理
     * @param block 协程体内要执行的挂起代码块
     * @return 返回代表该协程的 [Job] 对象
     */
    protected fun launch(
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val handler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
        return viewModelScope.launch(handler, block = block)
    }
}
