package com.app.video.user.page.auth.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.databinding.FragmentLoginBinding
import com.app.video.user.page.auth.AuthActivity
import kotlinx.coroutines.launch
/**
 * 登录页面 Fragment。
 *
 * 负责：
 * 1. 展示登录 UI
 * 2. 读取用户输入
 * 3. 点击按钮后调用 ViewModel
 * 4. 观察 ViewModel 状态
 * 5. 登录成功后通知 AuthActivity 跳转 MainActivity
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initClickListeners()
    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        renderState(state)
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        handleEvent(event)
                    }
                }

            }
        }
    }
    private fun initClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.login(
                username = username,
                password = password
            )
        }
        binding.tvGoRegister.setOnClickListener {
            (requireActivity() as? AuthActivity)?.openRegisterPage()
        }

        binding.tvForgotPassword.setOnClickListener {
            (requireActivity() as? AuthActivity)?.openForgotPasswordPage()
        }
    }
    private fun renderState(state: LoginUiState) {
        binding.btnLogin.isEnabled = !state.isLoading
        binding.etUsername.isEnabled = !state.isLoading
        binding.etPassword.isEnabled = !state.isLoading

        binding.btnLogin.text = if (state.isLoading) {
            "登录中..."
        } else {
            "登录"
        }
    }

    private fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowMessage -> {
                Toast.makeText(
                    requireContext(),
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            LoginEvent.LoginSuccess -> {
                (requireActivity() as? AuthActivity)?.goToMain()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
