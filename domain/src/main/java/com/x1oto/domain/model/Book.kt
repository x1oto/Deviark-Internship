package com.x1oto.domain.model

import com.x1oto.domain.utils.Genre

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val genre: Genre,
    val year: Short,
    var borrowCount: Int = 0,
    var isBorrowed: Boolean = false,
    var lastBorrowedTimestamp: Long? = null
)