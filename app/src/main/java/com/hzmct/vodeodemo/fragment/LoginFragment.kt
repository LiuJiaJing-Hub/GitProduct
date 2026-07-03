package com.hzmct.vodeodemo.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.hzmct.vodeodemo.R
import com.hzmct.vodeodemo.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val vm: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsername = view.findViewById<AppCompatEditText>(R.id.et_username)
        val etPassword = view.findViewById<AppCompatEditText>(R.id.et_password)
        val ivEye = view.findViewById<AppCompatImageView>(R.id.iv_eye)
        val btnLogin = view.findViewById<AppCompatButton>(R.id.btn_login)
        val forgotPassword = view.findViewById<AppCompatTextView>(R.id.tv_forgot_password)
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                vm.onUsernameChanged(s?.toString().orEmpty())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                vm.onPasswordChanged(s?.toString().orEmpty())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //点击后跳转到充值密码页面
        forgotPassword.setOnClickListener { view ->
            findNavController().navigate(R.id.registerFragment)
        }

        ivEye.setOnClickListener { vm.togglePasswordVisibility() }
        btnLogin.setOnClickListener { vm.login() }

        observeState(etPassword, ivEye)
    }

    /** 单一 StateFlow 同时处理 UI 更新 + Toast + 导航 */
    private fun observeState(etPassword: AppCompatEditText, ivEye: AppCompatImageView) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { state ->
                    // 密码显隐
                    if (state.isPasswordVisible) {
                        etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        ivEye.setImageResource(R.drawable.icon_eye_show)
                    } else {
                        etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        ivEye.setImageResource(R.drawable.icon_eye_closs)
                    }

                    // 错误提示
                    state.errorMessage?.let { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        vm.onErrorShown()
                    }
                    Log.d("LoginFragment", "UI State: $state")
                    // 登录成功 → 导航到首页
                    if (state.navigateToHome) {
                        vm.onNavigated()
                        Log.d("LoginFragment", "Navigating to home fragment")
                        findNavController().navigate(R.id.homeFragment)
                    }
                }
            }
        }
    }
}
