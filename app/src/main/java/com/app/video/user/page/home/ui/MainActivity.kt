package com.app.video.user.page.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.databinding.ActivityMainBinding
import com.app.video.user.page.auth.AuthActivity

/**
 * 应用的首页 Activity。
 * 负责作为容器加载主页的 Fragment。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Activity 创建时的回调。
     * 初始化视图绑定并将 [MainFragment] 放入容器中。
     *
     * @param savedInstanceState 恢复的实例状态包
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!TokenManager(this).isLogin()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainFragmentContainer.id, MainFragment())
                .commit()
        }
    }
}
