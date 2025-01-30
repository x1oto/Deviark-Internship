package com.x1oto.librarymangmentbook.presentation.mvi.home

import com.x1oto.librarymangmentbook.data.Book

sealed class HomeState {
    object Loading : HomeState()
    data class FetchBooksSuccess(val books: List<Book>) : HomeState()
    data class FetchPopularBooksSuccess(val books: List<Book>) : HomeState()
    data class FetchUnPopularBooksSuccess(val books: List<Book>) : HomeState()
    data class Error(val message: String) : HomeState()
}