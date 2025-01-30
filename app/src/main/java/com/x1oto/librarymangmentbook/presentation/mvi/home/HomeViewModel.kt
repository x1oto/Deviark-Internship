package com.x1oto.librarymangmentbook.presentation.mvi.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Database
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Too tight coupling. What if i want to use sharedFlow only once?
    // Maybe use .asSharedFlow?
    private val _homeLiveData = MutableLiveData<HomeState>()
    val homeLiveData: LiveData<HomeState> get() = _homeLiveData

    // Send request when VM created.
    init {
        fetchBooks()
    }

    // In MVI we must have only one public method.
    // We will get different object in that fun depends what activity wants to do.

    fun send(event: HomeEvent) {
        when (event) {
            HomeEvent.ClearSortingCriteria -> {
                fetchBooks()
            }

            HomeEvent.GetMostPopularEvent -> {
                getMostPopularBook()
            }

            HomeEvent.GetLessPopularEvent -> {
                getLessPopularEvent()
            }
        }
    }

    private fun fetchBooks() {
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.FetchBooksSuccess(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun getMostPopularBook() {
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.getMostPopularBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.FetchPopularBooksSuccess(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun getLessPopularEvent() {
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.getLessPopularBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.FetchUnPopularBooksSuccess(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }
}