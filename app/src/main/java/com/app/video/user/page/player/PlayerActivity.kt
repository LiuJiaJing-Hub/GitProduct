package com.app.video.user.page.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.app.video.user.databinding.ActivityPlayerBinding
import kotlinx.coroutines.launch

/**
 * 新增视频播放页。
 *
 * 使用 Media3 ExoPlayer 播放详情页传入的 playUrl，并在页面销毁时释放播放器资源。
 */
class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModels()
    private var player: ExoPlayer? = null
    private var currentPlayUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlayerBack.setOnClickListener { finish() }
        observeViewModel()
        viewModel.loadPlayerData(
            videoId = intent.getStringExtra(EXTRA_VIDEO_ID).orEmpty(),
            episodeId = intent.getStringExtra(EXTRA_EPISODE_ID),
            playUrl = intent.getStringExtra(EXTRA_PLAY_URL),
            title = intent.getStringExtra(EXTRA_TITLE)
        )
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> renderState(state) }
            }
        }
    }

    private fun renderState(state: PlayerUiState) {
        binding.tvPlayerTitle.text = state.title
        binding.tvPlayerLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.tvPlayerError.visibility = if (state.errorMessage.isNullOrBlank()) View.GONE else View.VISIBLE
        binding.tvPlayerError.text = state.errorMessage.orEmpty()
        if (!state.isLoading && state.playUrl.isNotBlank() && state.playUrl != currentPlayUrl) {
            preparePlayer(state.playUrl)
        }
    }

    private fun preparePlayer(playUrl: String) {
        currentPlayUrl = playUrl
        val exoPlayer = player ?: ExoPlayer.Builder(this).build().also {
            player = it
            binding.playerView.player = it
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(playUrl))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_VIDEO_ID = "extra_video_id"
        private const val EXTRA_EPISODE_ID = "extra_episode_id"
        private const val EXTRA_PLAY_URL = "extra_play_url"
        private const val EXTRA_TITLE = "extra_title"

        fun start(
            context: Context,
            videoId: String,
            episodeId: String,
            playUrl: String,
            title: String
        ) {
            context.startActivity(
                Intent(context, PlayerActivity::class.java)
                    .putExtra(EXTRA_VIDEO_ID, videoId)
                    .putExtra(EXTRA_EPISODE_ID, episodeId)
                    .putExtra(EXTRA_PLAY_URL, playUrl)
                    .putExtra(EXTRA_TITLE, title)
            )
        }
    }
}
