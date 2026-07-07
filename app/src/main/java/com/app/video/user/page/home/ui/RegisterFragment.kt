package com.app.video.user.page.home.ui

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
import com.app.video.user.databinding.FragmentRegisterBinding
import com.app.video.user.page.home.viewmodel.RegisterEvent
import com.app.video.user.page.home.viewmodel.RegisterUiState
import com.app.video.user.page.home.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch
/**
 * 注册页面 Fragment。
 *
 * 负责：
 * 1. 展示注册 UI
 * 2. 读取用户输入
 * 3. 点击按钮后调用 RegisterViewModel
 * 4. 注册成功后返回登录页
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val viewModel: RegisterViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        observeViewModel()
    }
    // 观察 ViewModel
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
        binding.btnRegister.setOnClickListener {
            val username = binding.etRegisterUsername.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirmPassword = binding.etRegisterConfirmPassword.text.toString().trim()
            viewModel.register(username, password, confirmPassword)
        }
        binding.tvBackLogin.setOnClickListener {
            (requireActivity() as? AuthActivity)?.backToLoginPage()
        }
    }
    private fun renderState(state: RegisterUiState) {
        binding.btnRegister.isEnabled = !state.isLoading
        binding.etRegisterUsername.isEnabled = !state.isLoading
        binding.etRegisterPassword.isEnabled = !state.isLoading
        binding.etRegisterConfirmPassword.isEnabled = !state.isLoading

        binding.btnRegister.text = if (state.isLoading) {
            "注册中..."
        } else {
            "注册"
        }
    }

    private fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ShowMessage -> {
                Toast.makeText(
                    requireContext(),
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            RegisterEvent.RegisterSuccess -> {
                (requireActivity() as? AuthActivity)?.backToLoginPage()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
