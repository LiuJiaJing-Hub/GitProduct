package com.app.video.user.page.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.video.user.databinding.ActivityFavoriteBinding
import com.app.video.user.page.detail.VideoDetailActivity
import com.app.video.user.page.favorite.adapter.FavoriteVideoAdapter
import kotlinx.coroutines.launch

/**
 * 新增我的收藏页。
 *
 * 展示本地收藏的视频列表，并提供进入详情和移除收藏能力。
 */
class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private val adapter = FavoriteVideoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFavoriteBack.setOnClickListener { finish() }
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state -> renderState(state) }
                }
                launch {
                    viewModel.event.collect { event -> handleEvent(event) }
                }
            }
        }
    }

    private fun renderState(state: FavoriteUiState) {
        binding.tvFavoriteLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.favoriteListContainer.visibility =
            if (!state.isLoading && state.favorites.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tvFavoriteEmpty.visibility =
            if (!state.isLoading && state.favorites.isEmpty() && state.errorMessage.isNullOrBlank()) View.VISIBLE else View.GONE
        binding.tvFavoriteError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvFavoriteError.text = state.errorMessage.orEmpty()

        adapter.submit(
            context = this,
            container = binding.favoriteListContainer,
            favorites = state.favorites,
            onItemClick = viewModel::openDetail,
            onRemoveClick = viewModel::removeFavorite
        )
    }

    private fun handleEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.NavigateDetail -> VideoDetailActivity.start(this, event.videoId)
            is FavoriteEvent.ShowMessage -> Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, FavoriteActivity::class.java))
        }
    }
}
