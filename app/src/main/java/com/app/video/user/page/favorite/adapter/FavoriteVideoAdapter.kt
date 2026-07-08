package com.app.video.user.page.favorite.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.domain.model.FavoriteVideoItem

/**
 * 新增收藏列表适配器，支持点击详情和单项移除。
 */
class FavoriteVideoAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        favorites: List<FavoriteVideoItem>,
        onItemClick: (String) -> Unit,
        onRemoveClick: (String) -> Unit
    ) {
        container.removeAllViews()
        favorites.forEach { favorite ->
            container.addView(
                createFavoriteCard(context, favorite, onItemClick, onRemoveClick),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = context.dp(12) }
            )
        }
    }

    private fun createFavoriteCard(
        context: Context,
        favorite: FavoriteVideoItem,
        onItemClick: (String) -> Unit,
        onRemoveClick: (String) -> Unit
    ): LinearLayout {
        val video = favorite.video
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_favorite_card)
            setPadding(context.dp(12), context.dp(12), context.dp(12), context.dp(12))
            setOnClickListener { onItemClick(video.id) }

            addView(createCover(context, video.tag, video.isVip))
            addView(createInfo(context, favorite), LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            addView(createRemoveButton(context) { onRemoveClick(video.id) })
        }
    }

    private fun createCover(context: Context, tag: String, isVip: Boolean): TextView {
        return TextView(context).apply {
            text = if (isVip) "VIP" else tag
            gravity = Gravity.BOTTOM or Gravity.START
            textSize = 11f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.white))
            setBackgroundResource(R.drawable.bg_home_video_cover)
            setPadding(context.dp(8), context.dp(8), context.dp(8), context.dp(8))
            layoutParams = LinearLayout.LayoutParams(context.dp(96), context.dp(72)).apply {
                marginEnd = context.dp(12)
            }
        }
    }

    private fun createInfo(context: Context, favorite: FavoriteVideoItem): LinearLayout {
        val video = favorite.video
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(TextView(context).apply {
                text = video.title
                textSize = 16f
                maxLines = 1
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(context.getColorCompat(R.color.text_primary))
            })
            addView(TextView(context).apply {
                text = video.subTitle
                textSize = 13f
                maxLines = 1
                setTextColor(context.getColorCompat(R.color.text_secondary))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = context.dp(4) }
            })
            addView(TextView(context).apply {
                text = "${video.category} · ${video.score}分 · 收藏于 ${favorite.addedTime}"
                textSize = 12f
                maxLines = 1
                setTextColor(context.getColorCompat(R.color.text_secondary))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = context.dp(6) }
            })
        }
    }

    private fun createRemoveButton(context: Context, onClick: () -> Unit): TextView {
        return TextView(context).apply {
            text = "移除"
            gravity = Gravity.CENTER
            textSize = 13f
            setTextColor(context.getColorCompat(R.color.brand_primary))
            setBackgroundResource(R.drawable.bg_outline_button)
            setPadding(context.dp(12), 0, context.dp(12), 0)
            setOnClickListener {
                it.parent?.requestDisallowInterceptTouchEvent(true)
                onClick()
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                context.dp(36)
            ).apply { marginStart = context.dp(8) }
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
