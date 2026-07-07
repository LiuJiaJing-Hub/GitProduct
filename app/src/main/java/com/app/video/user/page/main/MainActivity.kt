package com.app.video.user.page.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.video.user.R
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.databinding.ActivityMainBinding
import com.app.video.user.page.auth.AuthActivity
import com.app.video.user.page.channel.ChannelFragment
import com.app.video.user.page.home.HomeFragment
import com.app.video.user.page.mine.MineFragment
import com.app.video.user.page.shortdrama.ShortDramaFragment
import com.app.video.user.page.vip.VipFragment

/**
 * 登录后的主框架。
 *
 * 负责承载五个底部 Tab： 首页、频道、短剧、会员、我的。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!TokenManager(this).isLogin()) {
//            startActivity(Intent(this, AuthActivity::class.java))
//            finish()
//            return
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.tabHome
            switchFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabHome -> switchFragment(HomeFragment())
                R.id.tabChannel -> switchFragment(ChannelFragment())
                R.id.tabShortDrama -> switchFragment(ShortDramaFragment())
                R.id.tabVip -> switchFragment(VipFragment())
                R.id.tabMine -> switchFragment(MineFragment())
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainer.id, fragment)
            .commit()
        return true
    }
}
