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
            HomeEvent.ClearSortingEvent -> {
                fetchBooks()
            }

            HomeEvent.GetMostPopularEvent -> {
                getMostPopularBook()
            }

            HomeEvent.GetLessPopularEvent -> {
                getLessPopularEvent()
            }

            is HomeEvent.SearchEvent -> {
                getBookByQuery(event.query)
            }

            HomeEvent.IncrementCountEvent -> { incrementFirstIndex() }
        }
    }

    private fun fetchBooks() {
        if(_homeLiveData.value is HomeState.Loading) {
            return
        }
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.Data(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun getMostPopularBook() {
        if(_homeLiveData.value is HomeState.Loading) {
            return
        }
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.getMostPopularBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.Data(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun getLessPopularEvent() {
        if(_homeLiveData.value is HomeState.Loading) {
            return
        }
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.getLessPopularBooks()
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.Data(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun getBookByQuery(query: String) {
        if(_homeLiveData.value is HomeState.Loading) {
            return
        }
        _homeLiveData.value = HomeState.Loading
        viewModelScope.launch {
            Database.searchBooksByQuery(query)
                .onSuccess { books ->
                    _homeLiveData.value = HomeState.Data(books = books)
                }
                .onFailure { e ->
                    _homeLiveData.value = HomeState.Error(e.message.toString())
                }
        }
    }

    private fun incrementFirstIndex() {
        val currentState = _homeLiveData.value
        if(currentState is HomeState.Data) {
            viewModelScope.launch {
                if (currentState.books.isEmpty()) {
                    _homeLiveData.value = HomeState.Error("Cannot increment, somehow books list is empty")
                } else {
                    val updatedBooks = currentState.books.toMutableList().apply {
                        this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                    }
                    _homeLiveData.value = HomeState.Data(books = updatedBooks)
                }
            }
        }

    }
}