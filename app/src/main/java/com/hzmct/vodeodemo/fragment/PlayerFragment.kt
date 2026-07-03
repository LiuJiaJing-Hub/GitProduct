package com.hzmct.vodeodemo.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.navigation.fragment.findNavController
import com.hzmct.vodeodemo.ExoPlayerManager
import com.hzmct.vodeodemo.R
import com.hzmct.vodeodemo.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val vm: PlayerViewModel by viewModels()
    private var isFullscreen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playerView = view.findViewById<PlayerView>(R.id.playerView)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnFullscreen = view.findViewById<ImageButton>(R.id.btnFullscreen)
        val player = ExoPlayerManager.get(requireContext())

        playerView.player = player

        val url = arguments?.getString("videoUrl") ?: return
        vm.setVideo(url)

        lifecycleScope.launch {
            vm.state.collect { state ->
                state.videoUrl?.let {
                    player.setMediaItem(MediaItem.fromUri(it))
                    player.prepare()
                    player.playWhenReady = true
                }
            }
        }

        // 进入播放页时隐藏系统导航栏
        showSystemUI(false)

        // 返回按钮
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // 横竖屏切换按钮（左下角）
        btnFullscreen.setOnClickListener {
            toggleFullscreen()
        }
    }

    /**
     * 切换横屏全屏 / 竖屏
     */
    private fun toggleFullscreen() {
        val activity = requireActivity()
        if (isFullscreen) {
            // 切回竖屏
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isFullscreen = false
            showSystemUI(false) // 竖屏也保持沉浸，不显示系统导航栏
        } else {
            // 切为横屏全屏，隐藏所有系统栏
            showSystemUI(false)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            isFullscreen = true
        }
    }

    /**
     * 隐藏 / 显示系统状态栏和导航栏
     */
    private fun showSystemUI(show: Boolean) {
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        )
        if (show) {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        } else {
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onStop() {
        super.onStop()
        // 退出播放页时恢复竖屏和系统栏
        if (isFullscreen) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isFullscreen = false
        }
        showSystemUI(true)
    }
}
