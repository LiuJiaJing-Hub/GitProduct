package com.app.video.user.page.channel

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
import com.app.video.user.databinding.FragmentChannelBinding
import com.app.video.user.domain.model.ChannelSelection
import com.app.video.user.domain.model.ChannelVideoItem
import com.app.video.user.domain.model.FilterOption
import com.app.video.user.page.channel.adapter.ChannelVideoAdapter
import kotlinx.coroutines.launch

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChannelViewModel by viewModels()
    private val videoAdapter = ChannelVideoAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChannelBinding.inflate(inflater, container, false)
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

    private fun renderState(state: ChannelUiState) {
        binding.tvChannelLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.channelContentContainer.visibility = if (state.isLoading) View.GONE else View.VISIBLE
        binding.tvChannelError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvChannelError.text = state.errorMessage.orEmpty()

        renderFilterRow(
            container = binding.categoryContainer,
            options = state.categories,
            selectedId = state.selection.categoryId,
            onClick = viewModel::selectCategory
        )
        renderFilterRow(
            container = binding.areaContainer,
            options = state.areas,
            selectedId = state.selection.areaId,
            onClick = viewModel::selectArea
        )
        renderFilterRow(
            container = binding.yearContainer,
            options = state.years,
            selectedId = state.selection.yearId,
            onClick = viewModel::selectYear
        )
        renderFilterRow(
            container = binding.sortContainer,
            options = state.sorts,
            selectedId = state.selection.sortId,
            onClick = viewModel::selectSort
        )
        renderResultSummary(state.selection, state.videos)
        videoAdapter.submit(
            context = requireContext(),
            container = binding.channelVideoContainer,
            videos = state.videos
        )
    }

    private fun renderFilterRow(
        container: LinearLayout,
        options: List<FilterOption>,
        selectedId: String,
        onClick: (String) -> Unit
    ) {
        container.removeAllViews()
        options.forEach { option ->
            val selected = option.id == selectedId
            val view = TextView(requireContext()).apply {
                text = option.name
                textSize = 14f
                setTextColor(resources.getColor(if (selected) R.color.white else R.color.text_primary, null))
                setBackgroundResource(if (selected) R.drawable.bg_channel_filter_selected else R.drawable.bg_channel_filter)
                setPadding(dp(14), dp(8), dp(14), dp(8))
                setOnClickListener { onClick(option.id) }
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = dp(8)
            }
            container.addView(view, params)
        }
    }

    private fun renderResultSummary(selection: ChannelSelection, videos: List<ChannelVideoItem>) {
        val sortName = when (selection.sortId) {
            "new" -> "最新"
            "score" -> "评分最高"
            else -> "最热"
        }
        binding.tvChannelResult.text = "共 ${videos.size} 部内容 · $sortName"
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
