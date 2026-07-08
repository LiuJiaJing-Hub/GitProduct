package com.app.video.user.page.vip

import android.os.Bundle
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
import com.app.video.user.databinding.FragmentVipBinding
import com.app.video.user.domain.model.VipBenefit
import com.app.video.user.domain.model.VipData
import com.app.video.user.domain.model.VipRecommendItem
import com.app.video.user.page.detail.VideoDetailActivity
import com.app.video.user.page.vip.adapter.VipPlanAdapter
import kotlinx.coroutines.launch

class VipFragment : Fragment() {

    private var _binding: FragmentVipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VipViewModel by viewModels()
    private val planAdapter = VipPlanAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnOpenVip.setOnClickListener { viewModel.openSelectedPlan() }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state -> renderState(state) }
                }
                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is VipEvent.ShowMessage -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun renderState(state: VipUiState) {
        binding.tvVipLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.vipContentContainer.visibility = if (state.vipData == null) View.GONE else View.VISIBLE
        binding.tvVipError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvVipError.text = state.errorMessage.orEmpty()
        state.vipData?.let { renderVipData(it, state.selectedPlanId) }
    }

    private fun renderVipData(data: VipData, selectedPlanId: String) {
        binding.tvVipIdentity.text = data.userStatus.title
        binding.tvVipExpire.text = data.userStatus.expireTime ?: "开通会员享抢先看、高清和专属内容"
        renderBenefits(data.benefits)
        planAdapter.submit(
            context = requireContext(),
            container = binding.vipPlanContainer,
            plans = data.plans,
            selectedPlanId = selectedPlanId,
            onClick = viewModel::selectPlan
        )
        renderRecommends(data.recommends)
    }

    private fun renderBenefits(benefits: List<VipBenefit>) {
        binding.vipBenefitContainer.removeAllViews()
        benefits.forEach { benefit ->
            val view = TextView(requireContext()).apply {
                text = "${benefit.title}\n${benefit.description}"
                textSize = 13f
                setTextColor(resources.getColor(R.color.text_primary, null))
                setBackgroundResource(R.drawable.bg_vip_benefit)
                setPadding(dp(12), dp(10), dp(12), dp(10))
            }
            binding.vipBenefitContainer.addView(
                view,
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginEnd = dp(8)
                    bottomMargin = dp(8)
                }
            )
        }
    }

    private fun renderRecommends(recommends: List<VipRecommendItem>) {
        binding.vipRecommendContainer.removeAllViews()
        recommends.forEach { item ->
            val view = TextView(requireContext()).apply {
                text = "${item.title}  ${item.score}分\n${item.subTitle} · ${item.tag}"
                textSize = 14f
                setTextColor(resources.getColor(R.color.text_primary, null))
                setBackgroundResource(R.drawable.bg_vip_recommend)
                setPadding(dp(14), dp(12), dp(14), dp(12))
                setOnClickListener { VideoDetailActivity.start(requireContext(), item.id) }
            }
            binding.vipRecommendContainer.addView(
                view,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dp(10) }
            )
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
