package com.app.video.user.page.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.R
import com.app.video.user.databinding.FragmentHomeBinding
import com.app.video.user.domain.model.BannerItem
import com.app.video.user.domain.model.ChannelItem
import com.app.video.user.domain.model.HomeData
import com.app.video.user.page.home.adapter.VideoCardAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val videoCardAdapter = VideoCardAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: HomeUiState) {
        binding.tvHomeLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.homeContentContainer.visibility = if (state.homeData == null) View.GONE else View.VISIBLE
        binding.tvHomeError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvHomeError.text = state.errorMessage.orEmpty()

        state.homeData?.let { data ->
            renderHomeData(data)
        }
    }

    private fun renderHomeData(data: HomeData) {
        renderBanner(data.banners.firstOrNull())//firstOrNull()：安全取值，列表为空时返回 null 而非抛异常
        renderChannels(data.channels)
        videoCardAdapter.submit(
            context = requireContext(),
            container = binding.continueWatchingContainer,
            videos = data.continueWatching
        )

        binding.sectionsContainer.removeAllViews()
        data.sections.forEach { section ->
            val titleView = createSectionTitle(section.title)
            binding.sectionsContainer.addView(titleView)
            val sectionContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
            }
            binding.sectionsContainer.addView(sectionContainer)
            videoCardAdapter.submit(
                context = requireContext(),
                container = sectionContainer,
                videos = section.videos
            )
        }
    }

    private fun renderBanner(banner: BannerItem?) {
        binding.bannerContainer.visibility = if (banner == null) View.GONE else View.VISIBLE
        if (banner == null) {
            return
        }
        binding.tvBannerTag.text = banner.tag
        binding.tvBannerTitle.text = banner.title
        binding.tvBannerSubtitle.text = banner.subTitle
    }

    private fun renderChannels(channels: List<ChannelItem>) {
        binding.channelContainer.removeAllViews()
        channels.forEachIndexed { index, channel ->
            val view = TextView(requireContext()).apply {
                text = channel.name
                textSize = 14f
                setTextColor(resources.getColor(if (index == 0) R.color.white else R.color.text_primary, null))
                setBackgroundResource(if (index == 0) R.drawable.bg_home_channel_selected else R.drawable.bg_home_channel)
                setPadding(dp(14), dp(8), dp(14), dp(8))
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = dp(8)
            }
            binding.channelContainer.addView(view, params)
        }
    }

    private fun createSectionTitle(title: String): TextView {
        return TextView(requireContext()).apply {
            text = title
            textSize = 20f
            setTextColor(resources.getColor(R.color.text_primary, null))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(24)
                bottomMargin = dp(12)
            }
            layoutParams = params
        }
    }
    // Convert dp to pixels
    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    // Clear binding to avoid memory leaks
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
