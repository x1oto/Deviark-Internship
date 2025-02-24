package com.x1oto.cleanlibraryapp.presentation.utlis

import com.x1oto.domain.model.Book
import com.x1oto.cleanlibraryapp.presentation.adapters.HomeRecyclerViewItem

fun List<Book>.toRecyclerViewItem(): List<HomeRecyclerViewItem> {
    val group = this.groupBy { it.title.first() }

    val modified = mutableListOf<HomeRecyclerViewItem>()

    for ((k, v) in group.entries) {
        modified.add(HomeRecyclerViewItem.Letter(k))

        v.forEach {
            modified.add(
                HomeRecyclerViewItem.Book(
                    id = it.id,
                    title = it.title,
                    author = it.author,
                    genre = it.genre,
                    year = it.year,
                    borrowCount = it.borrowCount,
                    isBorrowed = it.isBorrowed,
                    lastBorrowedTimestamp = it.lastBorrowedTimestamp
                )
            )
        }
    }

    return modified
}