package com.hzmct.vodeodemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hzmct.vodeodemo.bean.VideoItem

class MainActivity : AppCompatActivity() {
    private val videoAdapter = MyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter
        videoAdapter.setOnItemClickListener { videoItem ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("video_url", videoItem.videoUrl)
            intent.putExtra("video_title", videoItem.title)
            startActivity(intent)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                    // Scroll Down
                }else{
                    // Scroll Up
                }
            }
        })
        loadData();

    }
    private fun loadData() {
      val videos = listOf(VideoItem("短片1：大兔子", "https://www.w3schools.com/html/mov_bbb.mp4"),
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
        )
        videoAdapter.setVideos(videos)
    }

}