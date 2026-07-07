package com.app.video.user.page.auth

/**
 * @liuJiaJing
 */
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.app.video.user.core.auth.TokenManager
import com.app.video.user.core.service.NetService
import com.app.video.user.databinding.ActivityAuthBinding
import com.app.video.user.page.auth.forgot.ForgotPasswordFragment
import com.app.video.user.page.auth.login.LoginFragment
import com.app.video.user.page.auth.register.RegisterFragment
import com.app.video.user.page.main.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val tokenManager: TokenManager by lazy {
        TokenManager(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (tokenManager.isLogin()) {
            goToMain()
            return
        }

        if (savedInstanceState == null) {
            showLoginFragment()
        }
    }
    fun goToMain() {
        clearBackStack()
        NetService.startService(this)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
    fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.authFragmentContainer.id, LoginFragment())
            .commit()
    }

    fun openRegisterPage() {
        supportFragmentManager.beginTransaction()
            .replace(binding.authFragmentContainer.id, RegisterFragment())
            .addToBackStack("register")
            .commit()
    }

    fun openForgotPasswordPage() {
        supportFragmentManager.beginTransaction()
            .replace(binding.authFragmentContainer.id, ForgotPasswordFragment())
            .addToBackStack("forgot_password")
            .commit()
    }

    fun backToLoginPage() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            showLoginFragment()
        }
    }

}
