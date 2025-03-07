package com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.domain.model.Book
import com.x1oto.domain.usecases.DeleteBooksUC
import com.x1oto.domain.usecases.FetchBooksByRetrofitUC
import com.x1oto.domain.usecases.FetchBooksUC
import com.x1oto.domain.usecases.GetLessPopularBooksUC
import com.x1oto.domain.usecases.GetMostPopularBooksUC
import com.x1oto.domain.usecases.GetRandomBookUC
import com.x1oto.domain.usecases.SearchBooksByQueryUC
import com.x1oto.domain.usecases.UpdateBooksUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRandomBookUC: GetRandomBookUC,
    private val fetchBooksUC: FetchBooksUC,
    private val getMostPopularBooksUC: GetMostPopularBooksUC,
    private val getLessPopularBooksUC: GetLessPopularBooksUC,
    private val searchBooksByQueryUC: SearchBooksByQueryUC,
    private val updateBooksUC: UpdateBooksUC,
    private val deleteBooksUC: DeleteBooksUC,
    private val fetchBooksByRetrofitUC: FetchBooksByRetrofitUC

) : ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    private val tempBooks = mutableListOf<Book>()

    init {
        fetchBooks()
    }

    fun addBook(books: List<Book>) {
        viewModelScope.launch {
            val withNewBook = books.toMutableList().apply {
                add(getRandomBookUC())
            }
            _booksLiveData.value = withNewBook
            sendToBackEnd(withNewBook)
        }
    }

    fun saveTemporaryBooks() {
        viewModelScope.launch {
            tempBooks.add(getRandomBookUC())
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
            updateBooksUC(merged)
                .onFailure {
                    withContext(Dispatchers.Main) {
                        _errorSharedFlow.emit("Error uploading to back. Re add book.")
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
            fetchBooksByRetrofitUC()
            fetchBooksUC().collectLatest { result ->
                result
                    .onSuccess { books ->
                        _loadingLiveData.value = false
                        _booksLiveData.value = books
                    }
                    .onFailure { e ->
                        _loadingLiveData.value = false
                        _errorSharedFlow.emit(e.message.toString())
                    }
            }
        }
    }

    fun getMostPopularBook() {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            getMostPopularBooksUC().collectLatest { result ->
                result
                    .onSuccess { books ->
                        _loadingLiveData.value = false
                        _booksLiveData.value = books
                    }
                    .onFailure { e ->
                        _loadingLiveData.value = false
                        _errorSharedFlow.emit(e.message.toString())
                    }
            }

        }
    }

    fun getLessPopularEvent() {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            getLessPopularBooksUC().collectLatest { result ->
                result
                    .onSuccess { books ->
                        _loadingLiveData.value = false
                        _booksLiveData.value = books
                    }
                    .onFailure { e ->
                        _loadingLiveData.value = false
                        _errorSharedFlow.emit(e.message.toString())
                    }
            }

        }
    }

    fun getBookByQuery(query: String) {
        if (_loadingLiveData.value == true) {
            return
        }
        _loadingLiveData.value = true
        viewModelScope.launch {
            searchBooksByQueryUC(query).collectLatest { result ->
                result
                    .onSuccess { books ->
                        _loadingLiveData.value = false
                        _booksLiveData.value = books
                    }
                    .onFailure { e ->
                        _loadingLiveData.value = false
                        _errorSharedFlow.emit(e.message.toString())
                    }
            }

        }
    }

    fun incrementFirstIndex() {
        val currentState = _booksLiveData.value
        viewModelScope.launch {
            if (currentState.isNullOrEmpty()) {
                _errorSharedFlow.emit("Cannot increment, somehow books list is empty")
            } else {
                val updatedBooks = currentState.toMutableList().apply {
                    this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                }
                _booksLiveData.value = updatedBooks
            }
        }
    }

    fun deleteBookWithIds(toDeleteIds: List<Long>) {
        deleteBooksUC(toDeleteIds)
        fetchBooks()
    }
}