package com.hzmct.vodeodemo

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hzmct.vodeodemo.bean.VideoItem

/**
 * 首页适配器 —— 支持两种视图类型：
 *   TYPE_BANNER  — 顶部轮播图（ViewPager2 自动滚动）
 *   TYPE_GRID    — 3 列网格视频卡片（封面图 + 标题）
 */
class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_GRID = 1
        private const val BANNER_AUTO_SCROLL_INTERVAL = 3000L
    }

    private var videos: List<VideoItem> = emptyList()
    private var bannerVideos: List<VideoItem> = emptyList()
    private var onItemClick: ((VideoItem) -> Unit)? = null

    // ==================== 公开方法 ====================

    fun setVideos(videos: List<VideoItem>) {
        this.videos = videos
        this.bannerVideos = if (videos.size >= 5) videos.take(5) else videos
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (VideoItem) -> Unit) {
        this.onItemClick = listener
    }

    // ==================== Adapter 核心 ====================

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_BANNER else TYPE_GRID
    }

    override fun getItemCount(): Int {
        // position 0 = banner header, positions 1..n = grid items
        return if (videos.isEmpty()) 0 else 1 + videos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_BANNER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_banner_container, parent, false)
            BannerViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video_grid, parent, false)
            GridViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> bindBanner(holder)
            is GridViewHolder -> {
                val video = videos[position - 1] // position 0 是 banner
                bindGrid(holder, video)
            }
        }
    }

    // ==================== 轮播图绑定 ====================

    private fun bindBanner(holder: BannerViewHolder) {
        if (bannerVideos.isEmpty()) return

        val ctx = holder.itemView.context
        val viewPager = holder.viewPager

        val bannerAdapter = BannerPageAdapter(bannerVideos) { video ->
            onItemClick?.invoke(video)
        }
        viewPager.adapter = bannerAdapter

        // 自动滚动
        val handler = Handler(Looper.getMainLooper())
        var currentPage = 0
        val runnable = object : Runnable {
            override fun run() {
                if (bannerVideos.isNotEmpty()) {
                    currentPage = (currentPage + 1) % bannerVideos.size
                    viewPager.setCurrentItem(currentPage, true)
                }
                handler.postDelayed(this, BANNER_AUTO_SCROLL_INTERVAL)
            }
        }
        handler.postDelayed(runnable, BANNER_AUTO_SCROLL_INTERVAL)

        // 页面销毁时取消自动滚动
        viewPager.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                handler.removeCallbacks(runnable)
            }
        })
    }

    // ==================== 网格项绑定 ====================

    private fun bindGrid(holder: GridViewHolder, video: VideoItem) {
        val ctx = holder.itemView.context
        holder.tvTitle.text = video.title

        // Glide 加载封面图，失败时用深色占位
        Glide.with(ctx)
            .load(video.coverUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivCover)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(video)
        }
    }

    // ==================== ViewHolder ====================

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPager: ViewPager2 = itemView.findViewById(R.id.viewPagerBanner)
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    // ==================== 轮播图内部适配器 ====================

    private class BannerPageAdapter(
        private val items: List<VideoItem>,
        private val onClick: (VideoItem) -> Unit
    ) : RecyclerView.Adapter<BannerPageAdapter.PageHolder>() {

        override fun getItemCount(): Int = items.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_banner, parent, false)
            return PageHolder(view)
        }

        override fun onBindViewHolder(holder: PageHolder, position: Int) {
            val video = items[position]
            val ctx = holder.itemView.context
            holder.tvTitle.text = video.title
            holder.tvIndicator.text = "${position + 1}/${items.size}"

            Glide.with(ctx)
                .load(video.coverUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.darker_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivBanner)

            holder.itemView.setOnClickListener { onClick(video) }
        }

        class PageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivBanner: ImageView = itemView.findViewById(R.id.ivBanner)
            val tvTitle: TextView = itemView.findViewById(R.id.tvBannerTitle)
            val tvIndicator: TextView = itemView.findViewById(R.id.tvPageIndicator)
        }
    }
}
