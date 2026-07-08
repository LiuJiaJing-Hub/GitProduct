package com.app.video.user.page.vip.adapter

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.app.video.user.R
import com.app.video.user.domain.model.VipPlan

class VipPlanAdapter {

    fun submit(
        context: Context,
        container: LinearLayout,
        plans: List<VipPlan>,
        selectedPlanId: String,
        onClick: (String) -> Unit
    ) {
        container.removeAllViews()
        plans.forEach { plan ->
            val selected = plan.id == selectedPlanId
            val view = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(if (selected) R.drawable.bg_vip_plan_selected else R.drawable.bg_vip_plan)
                setPadding(context.dp(14), context.dp(12), context.dp(14), context.dp(12))
                setOnClickListener { onClick(plan.id) }
                addView(createTitle(context, plan.title, plan.tag, selected))
                addView(createPrice(context, plan.price, plan.originPrice, selected))
                addView(createDescription(context, plan.description, selected))
            }
            container.addView(
                view,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = context.dp(10) }
            )
        }
    }

    private fun createTitle(context: Context, title: String, tag: String, selected: Boolean): TextView {
        return TextView(context).apply {
            text = "$title  $tag"
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(if (selected) R.color.white else R.color.text_primary))
        }
    }

    private fun createPrice(context: Context, price: String, originPrice: String, selected: Boolean): TextView {
        return TextView(context).apply {
            text = "$price  原价 $originPrice"
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getColorCompat(if (selected) R.color.white else R.color.brand_primary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(6) }
        }
    }

    private fun createDescription(context: Context, description: String, selected: Boolean): TextView {
        return TextView(context).apply {
            text = description
            textSize = 12f
            setTextColor(context.getColorCompat(if (selected) R.color.white else R.color.text_secondary))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.dp(4) }
        }
    }

    private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun Context.getColorCompat(colorRes: Int): Int = resources.getColor(colorRes, null)
}
