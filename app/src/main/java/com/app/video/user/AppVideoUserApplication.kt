package com.app.video.user

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.core.service.NetService

/**
 * 应用程序的入口类。
 * 负责全局初始化工作，如网络服务、全局 Context 提取等。
 */
class AppVideoUserApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        /**
         * 获取全局的 Application Context。
         *
         * @return Application Context
         */
        fun getAppContext(): Context {
            return context
        }
    }

    /**
     * 应用程序创建时调用。
     * 初始化全局变量和后台服务。
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if (TokenManager(this).isLogin()) {
            NetService.startService(this)
        }
    }

    /**
     * 应用程序终止时调用。
     * 进行资源清理工作，如断开网络连接。
     */
    override fun onTerminate() {
        super.onTerminate()
        NetService.stopService(this)
    }
}
