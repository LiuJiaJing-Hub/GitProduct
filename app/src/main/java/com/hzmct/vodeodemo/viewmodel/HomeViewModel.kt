package com.hzmct.vodeodemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzmct.vodeodemo.bean.VideoItem
import com.hzmct.vodeodemo.service.SocketClientManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 首页 UI 状态
 */
data class HomeUiState(
    val videoList: List<VideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val error: String? = null
)

/**
 * 首页 ViewModel —— 通过 Socket 按需分页加载视频数据
 *
 * 先连接 Socket，请求首页(page=0)，展示前10条；
 * 滑动到底部触发 loadNextPage()，请求下一页(page=1)……直到最后一页。
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 0

    init {
//        viewModelScope.launch {
//            val connected = SocketClientManager.connect()
//            if (connected) {
//                loadNextPage()
//            } else {
//                _uiState.update { it.copy(error = "连接服务器失败") }
//            }
//        }
        val allVideos = listOf(
            // === Big Buck Bunny 系列 ===
            VideoItem("1", "大兔子 360p测试版", "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/320px-Big_buck_bunny_poster_big.jpg"),
            VideoItem("2", "大兔子完整版", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/320px-Big_buck_bunny_poster_big.jpg"),
            VideoItem("3", "大兔子 360p高清", "https://www.radiantmediaplayer.com/media/big-buck-bunny-360p.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/320px-Big_buck_bunny_poster_big.jpg"),
            VideoItem("4", "大兔子 720p", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4", ""),
            VideoItem("5", "大兔子 720p高清", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4", ""),
            VideoItem("6", "大兔子 720p超清", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4", ""),
            VideoItem("7", "大兔子 720p完整", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_10mb.mp4", ""),
            VideoItem("8", "大兔子 720p原画", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_20mb.mp4", ""),
            VideoItem("9", "大兔子 1080p", "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_1MB.mp4", ""),

            // === Sintel 系列 ===
            VideoItem("10", "辛特尔预告 1080p", "https://download.blender.org/durian/trailer/sintel_trailer-1080p.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Sintel_poster.jpg/320px-Sintel_poster.jpg"),
            VideoItem("11", "辛特尔 2K影院版", "https://download.blender.org/durian/movies/sintel-2048-surround.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Sintel_poster.jpg/320px-Sintel_poster.jpg"),
            VideoItem("12", "辛特尔 720p", "https://download.blender.org/durian/movies/sintel-1280-surround.mp4", ""),
            VideoItem("13", "辛特尔 标清", "https://download.blender.org/durian/movies/sintel-1024-surround.mp4", ""),

            // === Tears of Steel 系列 ===
            VideoItem("14", "钢铁之泪 战斗片段", "https://mango.blender.org/wp-content/uploads/2013/05/tears-of-steel-battle-clip-medium.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Tears_of_Steel_poster.jpg/320px-Tears_of_Steel_poster.jpg"),
            VideoItem("15", "钢铁之泪 完整版", "https://download.blender.org/mango/movies/tears-of-steel-1080p.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Tears_of_Steel_poster.jpg/320px-Tears_of_Steel_poster.jpg"),

            // === 其他开源动画 ===
            VideoItem("16", "大象之梦", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"),
            VideoItem("17", "Google测试-火焰", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"),
            VideoItem("18", "Google测试-逃脱", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"),
            VideoItem("19", "Google测试-乐趣", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg"),
            VideoItem("20", "Google测试-兜风", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg"),
        )
        _uiState.update { it.copy(videoList = allVideos) }
    }

    /**
     * 加载下一页数据（向服务端请求，然后追加到列表）
     */
    fun loadNextPage() {
        if (_uiState.value.isLoading || _uiState.value.isLastPage) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = SocketClientManager.requestPage(currentPage)
                if (response == null) {
                    _uiState.update { it.copy(isLoading = false, error = "服务器连接失败") }
                    return@launch
                }

                currentPage++

                _uiState.update { state ->
                    state.copy(
                        videoList = state.videoList + response.videos,
                        isLoading = false,
                        isLastPage = response.isLastPage
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    /**
     * 下拉刷新 —— 重置并重新连接加载
     */
    fun refresh() {
        currentPage = 0
        _uiState.value = HomeUiState()
        viewModelScope.launch {
            SocketClientManager.connect()
            loadNextPage()
        }
    }

    override fun onCleared() {
        super.onCleared()
        SocketClientManager.disconnect()
    }
}
