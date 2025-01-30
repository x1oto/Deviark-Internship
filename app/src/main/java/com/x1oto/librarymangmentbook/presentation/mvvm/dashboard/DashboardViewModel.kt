package com.x1oto.librarymangmentbook.presentation.mvvm.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.presentation.mvi.home.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        _isLoading.value = true
        viewModelScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    _isLoading.value = false
                    _books.value = books
                }
                .onFailure { e ->
                    _isLoading.value = false
                    _error.emit(e.message.toString())
                }
        }
    }

    fun incrementFirstIndex() {
        val currentState = _books.value
        viewModelScope.launch {
            if (currentState.isNullOrEmpty()) {
                _error.emit("Cannot increment, somehow books list is empty")
            } else {
                val updatedBooks = currentState.toMutableList().apply {
                    this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                }
                _books.value = updatedBooks
            }
        }
    }

    fun decrementFirstIndex() {
        val currentState = _books.value
        viewModelScope.launch {
            if (currentState.isNullOrEmpty()) {
                _error.emit("Cannot increment, somehow books list is empty")
            } else {
                val updatedBooks = currentState.toMutableList().apply {
                    this[0] = this[0].copy(borrowCount = this[0].borrowCount - 1)
                }
                _books.value = updatedBooks
            }
        }
    }

}