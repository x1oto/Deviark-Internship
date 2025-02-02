package com.x1oto.librarymangmentbook.presentation.mvvm.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.data.Database.getRandomBook
import com.x1oto.librarymangmentbook.presentation.mvi.home.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val tempBooks = mutableListOf<Book>()

    init {
        fetchBooks()
    }

    fun addBook(books: List<Book>) {
        viewModelScope.launch {
            val withNewBook = books.toMutableList().apply {
                add(getRandomBook())
            }
            _booksLiveData.value = withNewBook
            sendToBackEnd(withNewBook)
        }
    }

    fun saveTemporaryBooks() {
        viewModelScope.launch {
            tempBooks.add(getRandomBook())
        }
    }

    fun checkTemporaryBooks(books: List<Book>) {
        if (tempBooks.isNotEmpty()) {
            val merged = books.toMutableList().apply {
                addAll(tempBooks)
            }
            _booksLiveData.value = merged
            sendToBackEnd(merged)
            tempBooks.clear()
        }
    }

    private fun sendToBackEnd(merged: MutableList<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            Database.updateBooks(merged)
                .onFailure {
                    withContext(Dispatchers.Main) {
                        _errorLiveData.value = "Error uploading to back. Re add book."
                    }
                }
        }
    }

    fun fetchBooks() {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    _loadingLiveData.value = false
                    _booksLiveData.value = books
                }
                .onFailure { e ->
                    _loadingLiveData.value = false
                    _errorLiveData.value = e.message
                }
        }
    }

    fun getMostPopularBook() {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            Database.getMostPopularBooks()
                .onSuccess { books ->
                    _loadingLiveData.value = false
                    _booksLiveData.value = books
                }
                .onFailure { e ->
                    _loadingLiveData.value = false
                    _errorLiveData.value = e.message
                }
        }
    }

    fun getLessPopularEvent() {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            Database.getLessPopularBooks()
                .onSuccess { books ->
                    _loadingLiveData.value = false
                    _booksLiveData.value = books
                }
                .onFailure { e ->
                    _loadingLiveData.value = false
                    _errorLiveData.value = e.message
                }
        }
    }

    fun getBookByQuery(query: String) {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            Database.searchBooksByQuery(query)
                .onSuccess { books ->
                    _loadingLiveData.value = false
                    _booksLiveData.value = books
                }
                .onFailure { e ->
                    _loadingLiveData.value = false
                    _errorLiveData.value = e.message
                }
        }
    }

    fun incrementFirstIndex() {
        val currentState = _booksLiveData.value
            viewModelScope.launch {
                if (currentState.isNullOrEmpty()) {
                    _errorLiveData.value = "Cannot increment, somehow books list is empty"
                } else {
                    val updatedBooks = currentState.toMutableList().apply {
                        this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                    }
                    _booksLiveData.value = updatedBooks
                }
        }
    }

}