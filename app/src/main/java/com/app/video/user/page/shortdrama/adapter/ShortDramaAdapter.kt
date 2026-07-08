package com.app.video.user.page.shortdrama.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.domain.model.ShortDramaItem

class ShortDramaAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        dramas: List<ShortDramaItem>,
        onClick: (ShortDramaItem) -> Unit = {}
    ) {
        container.removeAllViews()
        dramas.chunked(2).forEach { rowItems ->
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
        item: ShortDramaItem,
        onClick: (ShortDramaItem) -> Unit
    ): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_short_drama_card)
            setPadding(context.dp(10), context.dp(10), context.dp(10), context.dp(10))
            setOnClickListener { onClick(item) }
            addView(createCover(context, item))
            addView(createTitle(context, item.title))
            addView(createSubTitle(context, item.subTitle))
            addView(createMeta(context, item))
        }
    }

    private fun createCover(context: Context, item: ShortDramaItem): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.BOTTOM
            setBackgroundResource(R.drawable.bg_short_drama_cover)
            setPadding(context.dp(8), context.dp(8), context.dp(8), context.dp(8))
            addView(createTag(context, if (item.isVip) "VIP" else item.tag))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                context.dp(132)
            )
        }
    }

    private fun createTag(context: Context, text: String): TextView {
        return TextView(context).apply {
            this.text = text
            textSize = 11f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.white))
            setBackgroundResource(R.drawable.bg_short_drama_tag)
            setPadding(context.dp(8), context.dp(4), context.dp(8), context.dp(4))
        }
    }

    private fun createTitle(context: Context, title: String): TextView {
        return TextView(context).apply {
            text = title
            textSize = 15f
            maxLines = 1
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.text_primary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(10) }
        }
    }

    private fun createSubTitle(context: Context, subTitle: String): TextView {
        return TextView(context).apply {
            text = subTitle
            textSize = 12f
            maxLines = 1
            setTextColor(context.getColorCompat(R.color.text_secondary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(4) }
        }
    }

    private fun createMeta(context: Context, item: ShortDramaItem): TextView {
        return TextView(context).apply {
            text = "${item.categoryName} · ${item.updatedCount}/${item.episodeCount}集 · ${item.playCount}"
            textSize = 11f
            maxLines = 1
            setTextColor(context.getColorCompat(R.color.text_secondary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(6) }
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
