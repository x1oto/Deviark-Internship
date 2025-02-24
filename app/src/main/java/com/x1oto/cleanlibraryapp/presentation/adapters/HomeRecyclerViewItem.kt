package com.x1oto.cleanlibraryapp.presentation.adapters

import com.x1oto.domain.utils.Genre

sealed interface HomeRecyclerViewItem {

    data class Letter(val char: Char) : HomeRecyclerViewItem

    data class Book(
        val id: Long,
        val title: String,
        val author: String,
        val genre: Genre,
        val year: Short,
        var borrowCount: Int = 0,
        var isBorrowed: Boolean = false,
        var lastBorrowedTimestamp: Long? = null,
    ) : HomeRecyclerViewItem

}