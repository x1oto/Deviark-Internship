package com.x1oto.librarymangmentbook.presentation.mvi.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.data.Database.getRandomBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _homeLiveData = MutableLiveData<HomeState>()
    val homeLiveData: LiveData<HomeState> get() = _homeLiveData

    private val tempBooks = mutableListOf<Book>()

    init {
        fetchBooks()
    }

    fun send(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchEvent -> getBookByQuery(event.query)
            HomeEvent.ClearSortingEvent -> fetchBooks()
            HomeEvent.GetMostPopularEvent -> getMostPopularBook()
            HomeEvent.GetLessPopularEvent -> getLessPopularEvent()
            HomeEvent.IncrementCountEvent -> incrementFirstIndex()
            HomeEvent.SaveTemporaryBooksEvent -> saveTemporaryBooks()
            is HomeEvent.CheckTemporaryBooksStatusEvent -> checkTemporaryBooks(event.books)
            is HomeEvent.AddBookEvent -> addBook(event.books)
        }
    }

    private fun addBook(books: List<Book>) {
        viewModelScope.launch {
            val withNewBook = books.toMutableList().apply {
                add(getRandomBook())
            }
            _homeLiveData.value = HomeState.Data(withNewBook)
            sendToBackEnd(withNewBook)
        }
    }

    private fun checkTemporaryBooks(books: List<Book>) {
        if (tempBooks.isNotEmpty()) {
            val merged = books.toMutableList().apply {
                addAll(tempBooks)
            }
            _homeLiveData.value = HomeState.Data(merged)
            sendToBackEnd(merged)
            tempBooks.clear()
        }
    }

    private fun sendToBackEnd(merged: MutableList<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            Database.updateBooks(merged)
                .onFailure {
                    withContext(Dispatchers.Main) {
                        _homeLiveData.value =
                            HomeState.Error("Error uploading to back. Re add book.")
                    }
                }
        }
    }

    private fun saveTemporaryBooks() {
        viewModelScope.launch {
            tempBooks.add(getRandomBook())
        }
    }

    private fun fetchBooks() {
        if (_homeLiveData.value is HomeState.Loading) {
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
        if (_homeLiveData.value is HomeState.Loading) {
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
        if (_homeLiveData.value is HomeState.Loading) {
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
        if (_homeLiveData.value is HomeState.Loading) {
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
        if (currentState is HomeState.Data) {
            viewModelScope.launch {
                if (currentState.books.isEmpty()) {
                    _homeLiveData.value =
                        HomeState.Error("Cannot increment, somehow books list is empty")
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