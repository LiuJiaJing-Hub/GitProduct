package com.app.video.user.page.channel.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.domain.model.ChannelVideoItem

class ChannelVideoAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        videos: List<ChannelVideoItem>,
        onClick: (ChannelVideoItem) -> Unit = {}
    ) {
        container.removeAllViews()
        if (videos.isEmpty()) {
            container.addView(createEmptyView(context))
            return
        }

        videos.chunked(2).forEach { rowItems ->
            val row = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
            container.addView(
                row,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = context.dp(12) }
            )

            rowItems.forEachIndexed { index, item ->
                row.addView(
                    createCard(context, item, onClick),
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                        if (index == 0) marginEnd = context.dp(10)
                    }
                )
            }

            if (rowItems.size == 1) {
                row.addView(View(context), LinearLayout.LayoutParams(0, 1, 1f))
            }
        }
    }

    private fun createCard(
        context: Context,
        item: ChannelVideoItem,
        onClick: (ChannelVideoItem) -> Unit
    ): LinearLayout {
        val video = item.video
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_channel_video_card)
            setPadding(context.dp(10), context.dp(10), context.dp(10), context.dp(10))
            setOnClickListener { onClick(item) }
            addView(createCover(context, item))
            addView(createTitle(context, video.title))
            addView(createSubtitle(context, video.subTitle))
            addView(createMeta(context, item))
        }
    }

    private fun createCover(context: Context, item: ChannelVideoItem): LinearLayout {
        val video = item.video
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.BOTTOM
            setBackgroundResource(R.drawable.bg_channel_video_cover)
            setPadding(context.dp(8), context.dp(8), context.dp(8), context.dp(8))
            addView(createTag(context, if (video.isVip) "VIP" else video.tag))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                context.dp(112)
            )
        }
    }

    private fun createTag(context: Context, text: String): TextView {
        return TextView(context).apply {
            this.text = text
            textSize = 11f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.white))
            setBackgroundResource(R.drawable.bg_channel_video_tag)
            setPadding(context.dp(8), context.dp(4), context.dp(8), context.dp(4))
        }
    }

    private fun createTitle(context: Context, title: String): TextView {
        return TextView(context).apply {
            text = title
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.text_primary))
            maxLines = 1
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(10) }
        }
    }

    private fun createSubtitle(context: Context, subtitle: String): TextView {
        return TextView(context).apply {
            text = subtitle
            textSize = 12f
            setTextColor(context.getColorCompat(R.color.text_secondary))
            maxLines = 1
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(4) }
        }
    }

    private fun createMeta(context: Context, item: ChannelVideoItem): TextView {
        val video = item.video
        return TextView(context).apply {
            text = "${video.category} · ${video.playCount} · ${video.score}分"
            textSize = 11f
            setTextColor(context.getColorCompat(R.color.text_secondary))
            maxLines = 1
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(6) }
        }
    }

    private fun createEmptyView(context: Context): TextView {
        return TextView(context).apply {
            text = "当前筛选下暂无内容"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(context.getColorCompat(R.color.text_secondary))
            setPadding(0, context.dp(40), 0, context.dp(40))
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
