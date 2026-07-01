package com.hzmct.vodeodemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hzmct.vodeodemo.bean.VideoItem


class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var videos : List<VideoItem> = emptyList()

     // 点击回调：把视频数据传出去
    private var onItemClick: ((VideoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder,
        position: Int) {
        val video = videos[position]
        holder.tvTitle.text = video.title
        // 点击整个项 → 跳转播放页
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(video)//把当前视频数据传出去
        }

    }

    override fun getItemCount(): Int {
        return videos.size
    }
    // ViewHolder绑定视图
    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
    // ========== 设置数据 ==========
    fun setVideos(videos: List<VideoItem>) {
        this.videos = videos
        notifyDataSetChanged()
    }

    fun addVideos(moreVideos: List<VideoItem>) {
        val start = videos.size
        videos = videos + moreVideos
        notifyItemRangeInserted(start, moreVideos.size)
    }
    // ========== 设置点击监听 ==========
    fun setOnItemClickListener(listener: (VideoItem) -> Unit) {
        this.onItemClick = listener
    }
}