package com.app.video.user.page.shortdrama

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
import com.app.video.user.databinding.FragmentShortDramaBinding
import com.app.video.user.domain.model.ShortDramaCategory
import com.app.video.user.page.detail.VideoDetailActivity
import com.app.video.user.page.shortdrama.adapter.ShortDramaAdapter
import kotlinx.coroutines.launch

class ShortDramaFragment : Fragment() {

    private var _binding: FragmentShortDramaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShortDramaViewModel by viewModels()
    private val adapter = ShortDramaAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShortDramaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> renderState(state) }
            }
        }
    }

    private fun renderState(state: ShortDramaUiState) {
        binding.tvShortDramaLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.shortDramaContentContainer.visibility = if (state.isLoading) View.GONE else View.VISIBLE
        binding.tvShortDramaError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvShortDramaError.text = state.errorMessage.orEmpty()
        renderCategories(state.categories, state.selectedCategoryId)
        binding.tvShortDramaCount.text = "共 ${state.dramas.size} 部短剧"
        adapter.submit(
            context = requireContext(),
            container = binding.shortDramaListContainer,
            dramas = state.dramas,
            onClick = { item -> VideoDetailActivity.start(requireContext(), item.id) }
        )
    }

    private fun renderCategories(categories: List<ShortDramaCategory>, selectedCategoryId: String) {
        binding.shortDramaCategoryContainer.removeAllViews()
        categories.forEach { category ->
            val selected = category.id == selectedCategoryId
            val view = TextView(requireContext()).apply {
                text = category.name
                textSize = 14f
                setTextColor(resources.getColor(if (selected) R.color.white else R.color.text_primary, null))
                setBackgroundResource(if (selected) R.drawable.bg_short_drama_filter_selected else R.drawable.bg_short_drama_filter)
                setPadding(dp(14), dp(8), dp(14), dp(8))
                setOnClickListener { viewModel.selectCategory(category.id) }
            }
            binding.shortDramaCategoryContainer.addView(
                view,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = dp(8) }
            )
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
