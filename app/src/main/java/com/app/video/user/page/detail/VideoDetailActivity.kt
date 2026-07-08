package com.app.video.user.page.detail

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
import com.app.video.user.R
import com.app.video.user.core.util.ImageLoadUtil
import com.app.video.user.databinding.ActivityVideoDetailBinding
import com.app.video.user.domain.model.VideoDetail
import com.app.video.user.page.detail.adapter.EpisodeAdapter
import com.app.video.user.page.detail.adapter.RecommendVideoAdapter
import com.app.video.user.page.player.PlayerActivity
import kotlinx.coroutines.launch

/**
 * 新增视频详情页。
 *
 * 承接首页、频道、短剧、收藏等入口，展示详情、选集、推荐并处理收藏和播放跳转。
 */
class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoDetailBinding
    private val viewModel: VideoDetailViewModel by viewModels()
    private val episodeAdapter = EpisodeAdapter()
    private val recommendAdapter = RecommendVideoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDetailBack.setOnClickListener { finish() }
        binding.btnPlay.setOnClickListener { viewModel.playSelectedEpisode() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }

        observeViewModel()
        viewModel.loadVideo(intent.getStringExtra(EXTRA_VIDEO_ID).orEmpty())
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

    private fun renderState(state: VideoDetailUiState) {
        binding.tvDetailLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.detailContentContainer.visibility = if (state.detail == null) View.GONE else View.VISIBLE
        binding.tvDetailError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvDetailError.text = state.errorMessage.orEmpty()
        state.detail?.let { renderDetail(it, state.selectedEpisodeId) }
    }

    private fun renderDetail(detail: VideoDetail, selectedEpisodeId: String?) {
        ImageLoadUtil.loadCover(binding.ivDetailCover, detail.video.coverUrl, R.drawable.bg_video_detail_cover)
        binding.tvDetailTitle.text = detail.video.title
        binding.tvDetailMeta.text = "${detail.video.category} · ${detail.year} · ${detail.area} · ${detail.video.score}分"
        binding.tvDetailSubTitle.text = detail.video.subTitle
        binding.tvDetailDescription.text = detail.description
        binding.tvDetailCreators.text = "导演：${detail.director}\n主演：${detail.actors}"
        binding.tvFavoriteCount.text = "${detail.favoriteCount} 人收藏"
        binding.btnFavorite.text = if (detail.isFavorite) "已收藏" else "收藏"

        episodeAdapter.submit(
            context = this,
            container = binding.episodeContainer,
            episodes = detail.episodes,
            selectedEpisodeId = selectedEpisodeId,
            onClick = viewModel::selectEpisode
        )

        recommendAdapter.submit(
            context = this,
            container = binding.recommendContainer,
            videos = detail.recommends,
            onClick = { videoId -> start(this, videoId) }
        )
    }

    private fun handleEvent(event: VideoDetailEvent) {
        when (event) {
            is VideoDetailEvent.NavigatePlayer -> {
                PlayerActivity.start(
                    context = this,
                    videoId = event.videoId,
                    episodeId = event.episodeId,
                    playUrl = event.playUrl,
                    title = event.title
                )
            }

            is VideoDetailEvent.ShowMessage -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
/*
    companion object 是 Kotlin 的伴生对象，类似于 Java 的 static 成员——它属于类本身，而不是类的实例。
    它可以包含属性和方法，这些属性和方法可以被类名直接访问，而不需要创建类的实例。
 */
    companion object {
        private const val EXTRA_VIDEO_ID = "extra_video_id"

        fun start(context: Context, videoId: String) {
            context.startActivity(
                Intent(context, VideoDetailActivity::class.java)
                    .putExtra(EXTRA_VIDEO_ID, videoId)
            )
        }
    }
}
