package com.hzmct.vodeodemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzmct.vodeodemo.bean.VideoItem

class MainActivity : AppCompatActivity() {
    private val videoAdapter = MyAdapter()
    private lateinit var toolbar: Toolbar
    private val pageSize : Int = 5
    private var currentPage : Int = 0
    private var isLoading : Boolean = false // Loading flag
    private var isNoMoreData  : Boolean = false
    private var isRequesting: Boolean = false  // ✅ 防抖标志
    private val totalPages: Int
        get() = (allVideos.size + pageSize - 1) / pageSize// 计算总页数

    private val allVideos = mutableListOf<VideoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        toolbar = findViewById(R.id.toolbar)
        initView()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter
        videoAdapter.setOnItemClickListener { videoItem ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("video_url", videoItem.videoUrl)
            intent.putExtra("video_title", videoItem.title)
            startActivity(intent)
        }
        loadData()
        showPage(0)  // 显示第一页
         //监听滚动事件，实现分页
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int){
                super.onScrollStateChanged(recyclerView, newState)
                // ✅ 只在滚动停止时触发
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // ✅ 检查是否滚动到底部
                    if (!recyclerView.canScrollVertically(1)) {
                        Log.d("MainActivity", "滚动到底部")
                        if (!isLoading && currentPage < totalPages - 1) {
                            loadNextPage(recyclerView)
                        }
                        return
                    }
//                        // ✅ 使用 post 延迟执行，避免在滚动回调中修改数据
//                        recyclerView.post {
//                            if (!isLoading && !isNoMoreData) {
//                                //initData()//追加数据
//                            }
//                        }
                        if(!recyclerView.canScrollVertically(-1)){
                            Log.d("MainActivity", "滚动到顶部")
                            if (!isLoading && currentPage > 0) {
                                loadPrevPage(recyclerView)
                        }
                    }
                }
            }
        })
    }
// ========== 加载下一页 ==========
private fun loadNextPage(recyclerView: RecyclerView) {
    if (isLoading) return
    if (currentPage >= totalPages - 1) {
        Log.d("MainActivity", "已经是最后一页了")
        return
    }

    isLoading = true
    Log.d("MainActivity", "加载下一页: ${currentPage + 1}")

    // ✅ 先保存当前页面偏移量，用于恢复滚动位置
    val currentOffset = (recyclerView.layoutManager as LinearLayoutManager)
        .findFirstVisibleItemPosition()

    // 切换到下一页
    recyclerView.post {
        showPage(currentPage + 1)
        isLoading = false

        // ✅ 滚动到列表顶部，让用户看到新内容
        recyclerView.scrollToPosition(0)
    }
}

// ========== 加载上一页 ==========
private fun loadPrevPage(recyclerView: RecyclerView) {
    if (isLoading) return
    if (currentPage <= 0) {
        Log.d("MainActivity", "已经是第一页了")
        return
    }

    isLoading = true
    Log.d("MainActivity", "加载上一页: ${currentPage - 1}")

    recyclerView.post {
        showPage(currentPage - 1)
        isLoading = false

        // ✅ 滚动到列表顶部
        recyclerView.scrollToPosition(0)
    }
}
    // 加载数据
    private fun showPage(page: Int) {
       if(page<0 || page>=allVideos.size)return
        currentPage = page
        val startIndex : Int = page * pageSize // 计算起始索引
        val endIndex : Int = minOf(startIndex + pageSize, allVideos.size)
        Log.d("MainActivity", "加载数据: startIndex=$startIndex, endIndex=$endIndex, total=${allVideos.size}")
        if(startIndex>=allVideos.size){
            isNoMoreData=true
            return
        }
        val pageData=allVideos.subList(startIndex, endIndex)
        videoAdapter.setVideos(pageData)

    }
    //追加数据
    private fun initData(){
        if (isLoading || isNoMoreData || isRequesting) {
            Log.d("MainActivity", "loadMoreData 被拦截: isLoading=$isLoading, isNoMoreData=$isNoMoreData, isRequesting=$isRequesting")
            return
        }
        isLoading=true
        val startIndex : Int = currentPage * pageSize
        val endIndex : Int = minOf(startIndex + pageSize, allVideos.size)
        Log.d("MainActivity", "加载数据: startIndex=$startIndex, endIndex=$endIndex, total=${allVideos.size}")
        if(startIndex>=allVideos.size){
            isNoMoreData=true
            isLoading = false
            return
        }
        val pageData=allVideos.subList(startIndex, endIndex)
        if(startIndex==0){
            videoAdapter.setVideos(pageData)
        }else{
            videoAdapter.addVideos(pageData)
        }
        currentPage++
        isLoading = false
        isRequesting = false
        Log.d("MainActivity", "没有更多数据了")
        if(endIndex==allVideos.size){
            isNoMoreData=true
        }

    }
    private fun initView(){
        toolbar.setTitle("Video Player")
        Log.d("MainActivity", "所有数据加载完毕")

    }
    private fun loadData() {
        allVideos.addAll(listOf(
            VideoItem("短片1：大兔子", "https://www.w3schools.com/html/mov_bbb.mp4"),
            VideoItem("短片2：海洋鲸鱼", "https://vjs.zencdn.net/v/oceans.mp4"),
            VideoItem("短片3：辛特尔预告", "https://media.w3.org/2010/05/sintel/trailer.mp4"),

          VideoItem("短片4：Blender 基金会测试", "https://media.w3.org/2010/05/video/movie_300.mp4"),
          VideoItem("短片5：W3C 视频测试1", "https://www.w3schools.com/html/movie.mp4"),
          VideoItem("短片6：W3C 视频测试2", "https://www.w3schools.com/tags/movie.mp4"),
          VideoItem("短片7：Sample Video", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4"),
          VideoItem("短片8：Sample Video 2", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_2mb.mp4"),
          VideoItem("短片9：Sample Video 5", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_5mb.mp4"),
          VideoItem("短片10：MPEG 测试", "https://filesamples.com/samples/video/mp4/sample_640x360.mp4"),
          VideoItem("短片11：MPEG 测试2", "https://filesamples.com/samples/video/mp4/sample_960x400.mp4"),
          VideoItem("短片12：MPEG 测试3", "https://filesamples.com/samples/video/mp4/sample_1280x720.mp4"),
          VideoItem("短片13：Open Movie Project", "https://upload.wikimedia.org/wikipedia/commons/transcoded/c/c0/Big_Buck_Bunny_4K.webm/Big_Buck_Bunny_4K.webm.720p.vp9.webm")
        ))

    }

}