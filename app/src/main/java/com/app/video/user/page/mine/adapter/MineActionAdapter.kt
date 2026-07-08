package com.app.video.user.page.mine.adapter

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.domain.model.MineActionItem

class MineActionAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        actions: List<MineActionItem>,
        onClick: (String) -> Unit
    ) {
        container.removeAllViews()
        actions.forEach { action ->
            val view = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(R.drawable.bg_mine_action)
                setPadding(context.dp(16), context.dp(14), context.dp(16), context.dp(14))
                setOnClickListener { onClick(action.id) }
                addView(createTitle(context, action.title))
                addView(createDescription(context, action.description))
            }
            container.addView(view, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = context.dp(10) })
        }
    }

    private fun createTitle(context: Context, title: String): TextView {
        return TextView(context).apply {
            text = title
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(R.color.text_primary))
        }
    }

    private fun createDescription(context: Context, description: String): TextView {
        return TextView(context).apply {
            text = description
            textSize = 12f
            setTextColor(context.getColorCompat(R.color.text_secondary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(4) }
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
