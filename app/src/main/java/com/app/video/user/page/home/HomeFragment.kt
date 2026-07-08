package com.app.video.user.page.home

import android.graphics.Typeface
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
import com.app.video.user.domain.model.HomeSection
import com.app.video.user.domain.model.VideoItem
import com.app.video.user.page.detail.VideoDetailActivity
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
            renderHomeData(
                data = data,
                selectedChannelId = state.selectedChannelId,
                displayContinueWatching = state.displayContinueWatching,
                displaySections = state.displaySections
            )
        }
    }

    private fun renderHomeData(
        data: HomeData,
        selectedChannelId: String,
        displayContinueWatching: List<VideoItem>,
        displaySections: List<HomeSection>
    ) {
        renderBanner(data.banners.firstOrNull())
        renderChannels(data.channels, selectedChannelId)

        binding.tvContinueWatchingTitle.visibility =
            if (displayContinueWatching.isEmpty()) View.GONE else View.VISIBLE
        binding.continueWatchingContainer.visibility =
            if (displayContinueWatching.isEmpty()) View.GONE else View.VISIBLE
        videoCardAdapter.submit(
            context = requireContext(),
            container = binding.continueWatchingContainer,
            videos = displayContinueWatching,
            onClick = { VideoDetailActivity.start(requireContext(), it.id) }
        )

        binding.sectionsContainer.removeAllViews()
        if (displaySections.isEmpty()) {
            binding.sectionsContainer.addView(createEmptyView())
            return
        }
        displaySections.forEach { section ->
            binding.sectionsContainer.addView(createSectionTitle(section.title))
            val sectionContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
            }
            binding.sectionsContainer.addView(sectionContainer)
            videoCardAdapter.submit(
                context = requireContext(),
                container = sectionContainer,
                videos = section.videos,
                onClick = { VideoDetailActivity.start(requireContext(), it.id) }
            )
        }
    }

    private fun renderBanner(banner: BannerItem?) {
        binding.bannerContainer.visibility = if (banner == null) View.GONE else View.VISIBLE
        if (banner == null) return
        binding.tvBannerTag.text = banner.tag
        binding.tvBannerTitle.text = banner.title
        binding.tvBannerSubtitle.text = banner.subTitle
        binding.bannerContainer.setOnClickListener {
            VideoDetailActivity.start(requireContext(), banner.targetVideoId)
        }
    }

    private fun renderChannels(channels: List<ChannelItem>, selectedChannelId: String) {
        binding.channelContainer.removeAllViews()
        channels.forEach { channel ->
            val isSelected = channel.id == selectedChannelId
            val view = TextView(requireContext()).apply {
                text = channel.name
                textSize = 14f
                setTextColor(resources.getColor(if (isSelected) R.color.white else R.color.text_primary, null))
                setBackgroundResource(if (isSelected) R.drawable.bg_home_channel_selected else R.drawable.bg_home_channel)
                setPadding(dp(14), dp(8), dp(14), dp(8))
                setOnClickListener { viewModel.selectHomeChannel(channel.id) }
            }
            binding.channelContainer.addView(
                view,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = dp(8) }
            )
        }
    }

    private fun createSectionTitle(title: String): TextView {
        return TextView(requireContext()).apply {
            text = title
            textSize = 20f
            setTextColor(resources.getColor(R.color.text_primary, null))
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(24)
                bottomMargin = dp(12)
            }
        }
    }

    private fun createEmptyView(): TextView {
        return TextView(requireContext()).apply {
            text = "当前频道暂无内容"
            textSize = 14f
            setTextColor(resources.getColor(R.color.text_secondary, null))
            setPadding(0, dp(24), 0, dp(24))
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
