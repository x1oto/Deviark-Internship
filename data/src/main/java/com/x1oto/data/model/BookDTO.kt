package com.x1oto.data.model

import com.x1oto.domain.utils.Genre
import kotlinx.serialization.Serializable

@Serializable
data class BookDTO(
    val id: Long,
    val title: String,
    val author: String,
    val genre: Genre,
    val year: Short,
    var borrowCount: Int,
    var isBorrowed: Boolean,
    var lastBorrowedTimestamp: Long
)