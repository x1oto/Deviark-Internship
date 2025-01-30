package com.x1oto.librarymangmentbook.data

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