package com.x1oto.librarymangmentbook.presentation.mvi.home

sealed class HomeEvent {
    object ClearSortingCriteria: HomeEvent()
    object GetMostPopularEvent: HomeEvent()
    object GetLessPopularEvent: HomeEvent()
}