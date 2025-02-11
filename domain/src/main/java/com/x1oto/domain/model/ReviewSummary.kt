package com.x1oto.domain.model

data class ReviewSummary(
    val reviews: List<Review>,
    val size: Int,
    val average: Double
)
