package com.app.video.user.domain.model

data class ShortDramaCategory(
    val id: String,
    val name: String
)

data class ShortDramaItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val categoryId: String,
    val categoryName: String,
    val episodeCount: Int,
    val updatedCount: Int,
    val playCount: String,
    val heat: String,
    val tag: String,
    val isVip: Boolean
)

data class ShortDramaData(
    val categories: List<ShortDramaCategory>,
    val dramas: List<ShortDramaItem>
)
