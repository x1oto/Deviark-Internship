package com.x1oto.librarymangmentbook.presentation.mvvm.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.presentation.mvi.home.HomeState
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    init {
        fetchBooks()
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