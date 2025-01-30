package com.x1oto.librarymangmentbook.presentation.mvi.home

import com.x1oto.librarymangmentbook.data.Book

sealed class HomeState {
    object Loading : HomeState()
    data class Data(val books: List<Book>) : HomeState()
    data class Error(val message: String) : HomeState()
}