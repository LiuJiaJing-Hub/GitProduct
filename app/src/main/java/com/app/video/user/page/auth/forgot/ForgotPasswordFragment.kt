package com.app.video.user.page.auth.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.databinding.FragmentForgotPasswordBinding
import com.app.video.user.page.auth.AuthActivity
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        observeViewModel()
    }

    private fun initClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            viewModel.resetPassword(
                username = binding.etForgotUsername.text.toString(),
                password = binding.etForgotPassword.text.toString(),
                confirmPassword = binding.etForgotConfirmPassword.text.toString()
            )
        }
        binding.tvForgotBackLogin.setOnClickListener {
            (requireActivity() as? AuthActivity)?.backToLoginPage()
        }
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

    private fun renderState(state: ForgotPasswordUiState) {
        binding.btnResetPassword.isEnabled = !state.isLoading
        binding.etForgotUsername.isEnabled = !state.isLoading
        binding.etForgotPassword.isEnabled = !state.isLoading
        binding.etForgotConfirmPassword.isEnabled = !state.isLoading
        binding.btnResetPassword.text = if (state.isLoading) {
            "提交中..."
        } else {
            "重置密码"
        }
    }

    private fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.ShowMessage -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }

            ForgotPasswordEvent.ResetSuccess -> {
                (requireActivity() as? AuthActivity)?.backToLoginPage()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
