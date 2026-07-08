package com.app.video.user.core.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.app.video.user.R
import com.bumptech.glide.Glide

/**
 * 新增图片加载工具，统一封面图的占位图、失败图和裁剪方式。
 */
object ImageLoadUtil {

    fun loadCover(
        imageView: ImageView,
        imageUrl: String?,
        @DrawableRes placeholderRes: Int = R.drawable.bg_home_video_cover
    ) {
        if (imageUrl.isNullOrBlank()) {
            imageView.setImageResource(placeholderRes)
            return
        }

        Glide.with(imageView)
            .load(imageUrl)
            .placeholder(placeholderRes)
            .error(placeholderRes)
            .centerCrop()
            .into(imageView)
    }
}
