package com.x1oto.librarymangmentbook.presentation.mvi.home

sealed class HomeEvent {
    object ClearSortingEvent : HomeEvent()
    object GetMostPopularEvent : HomeEvent()
    object GetLessPopularEvent : HomeEvent()
    object IncrementCountEvent : HomeEvent()
    class SearchEvent(val query: String) : HomeEvent()
}