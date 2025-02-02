package com.x1oto.librarymangmentbook.presentation.mvi.home

import com.x1oto.librarymangmentbook.data.Book

sealed class HomeEvent {
    object ClearSortingEvent : HomeEvent()
    object GetMostPopularEvent : HomeEvent()
    object GetLessPopularEvent : HomeEvent()
    object IncrementCountEvent : HomeEvent()
    class AddBookEvent(val books: List<Book>) : HomeEvent()
    object SaveTemporaryBooksEvent : HomeEvent()
    class CheckTemporaryBooksStatusEvent(val books: List<Book>) : HomeEvent()
    class SearchEvent(val query: String) : HomeEvent()
}