package com.app.video.user.page.mine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.R
import com.app.video.user.core.service.NetService
import com.app.video.user.databinding.FragmentMineBinding
import com.app.video.user.domain.model.MineData
import com.app.video.user.page.auth.AuthActivity
import com.app.video.user.page.favorite.FavoriteActivity
import com.app.video.user.page.mine.adapter.MineActionAdapter
import kotlinx.coroutines.launch

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MineViewModel by viewModels()
    private val actionAdapter = MineActionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        renderMineData(state.mineData)
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

    private fun renderMineData(data: MineData) {
        binding.tvMineUsername.text = data.profile.username
        binding.tvMineUserId.text = "ID: ${data.profile.userId}"
        binding.tvMineVip.text = data.profile.vipText
        renderStats(data)
        actionAdapter.submit(
            context = requireContext(),
            container = binding.mineActionContainer,
            actions = data.actions,
            onClick = viewModel::onActionClick
        )
    }

    private fun renderStats(data: MineData) {
        binding.mineStatContainer.removeAllViews()
        data.stats.forEach { stat ->
            val view = TextView(requireContext()).apply {
                text = "${stat.value}\n${stat.title}"
                Log.d("MineFragment", "${stat.value}\n${stat.title}")
                textSize = 14f
                gravity = android.view.Gravity.CENTER
                setTextColor(resources.getColor(R.color.text_primary, null))
            }
            binding.mineStatContainer.addView(
                view,
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            )
        }
    }

    private fun handleEvent(event: MineEvent) {
        when (event) {
            MineEvent.LogoutSuccess -> {
                NetService.stopService(requireContext())
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }

            MineEvent.NavigateFavorite -> {
                FavoriteActivity.start(requireContext())
            }

            is MineEvent.ShowMessage -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
