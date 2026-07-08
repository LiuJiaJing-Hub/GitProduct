package com.app.video.user.page.detail.adapter

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.core.util.PlaybackTimeUtil
import com.app.video.user.domain.model.EpisodeItem

/**
 * 新增选集适配器，详情页通过它完成选集展示和选择状态更新。
 */
class EpisodeAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        episodes: List<EpisodeItem>,
        selectedEpisodeId: String?,
        onClick: (String) -> Unit
    ) {
        container.removeAllViews()
        episodes.forEach { episode ->
            val selected = episode.id == selectedEpisodeId
            val itemView = TextView(context).apply {
                text = "${episode.title}\n${PlaybackTimeUtil.formatDuration(episode.duration)}"
                textSize = 13f
                setTypeface(typeface, if (selected) Typeface.BOLD else Typeface.NORMAL)
                setTextColor(context.getColorCompat(if (selected) R.color.white else R.color.text_primary))
                setBackgroundResource(if (selected) R.drawable.bg_episode_selected else R.drawable.bg_episode_normal)
                setPadding(context.dp(14), context.dp(10), context.dp(14), context.dp(10))
                setOnClickListener { onClick(episode.id) }
            }
            container.addView(
                itemView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = context.dp(10) }
            )
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
