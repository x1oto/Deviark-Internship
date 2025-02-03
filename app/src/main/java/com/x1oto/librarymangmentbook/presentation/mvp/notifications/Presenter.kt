package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import androidx.lifecycle.viewModelScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.data.Database.getRandomBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Presenter {
    private val presenterScope = CoroutineScope(Dispatchers.Main)
    private var view: MyView? = null

    private var cachedBooks: List<Book> = emptyList()

    val tempBooks = mutableListOf<Book>()

    fun attachView(view: MyView) {
        this.view = view
        if (cachedBooks.isNotEmpty()) {
            view.hideLoading()
            view.showData(cachedBooks)
        }
    }

    fun detachView() {
        this.view = null
    }

    fun addBook() {
        presenterScope.launch {
            val withNewBook = cachedBooks.toMutableList().apply {
                add(getRandomBook())
            }
            cachedBooks = withNewBook
            sendToBackEnd(cachedBooks)
            view?.showData(withNewBook)
        }
    }

    fun saveTemporaryBooks() {
        presenterScope.launch {
            tempBooks.add(getRandomBook())
        }
    }

    fun checkTemporaryBooks(books: List<Book>): List<Book> {
        return if (tempBooks.isNotEmpty()) {
            val merged = books.toMutableList().apply {
                addAll(tempBooks)
            }
            tempBooks.clear()
            cachedBooks = merged
            sendToBackEnd(merged)
            merged
        } else {
            books
        }
    }

    private fun sendToBackEnd(merged: List<Book>) {
        presenterScope.launch(Dispatchers.IO) {
            Database.updateBooks(merged)
                .onFailure {
                    withContext(Dispatchers.Main) {
                        view?.showError("Error uploading to back. Re add book.")
                    }
                }
        }
    }

    fun fetchBooks(isLoading: Boolean) {
        if (isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    view?.apply {
                        cachedBooks = books
                        hideLoading()
                        showData(cachedBooks)
                    }
                }
                .onFailure { e ->
                    view?.apply {
                        hideLoading()
                        showError(e.message.toString())
                    }
                }
        }
    }

    fun getMostPopularBook(isLoading: Boolean) {
        if (isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.getMostPopularBooks()
                .onSuccess { books ->
                    view?.apply {
                        cachedBooks = books
                        hideLoading()
                        showData(cachedBooks)
                    }
                }
                .onFailure { e ->
                    view?.apply {
                        hideLoading()
                        showError(e.message.toString())
                    }
                }
        }
    }

    fun getLessPopularEvent(isLoading: Boolean) {
        if (isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.getLessPopularBooks()
                .onSuccess { books ->
                    view?.apply {
                        cachedBooks = books
                        hideLoading()
                        showData(cachedBooks)
                    }
                }
                .onFailure { e ->
                    view?.apply {
                        hideLoading()
                        showError(e.message.toString())
                    }
                }
        }
    }

    fun getBookByQuery(isLoading: Boolean, query: String) {
        if (isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.searchBooksByQuery(query)
                .onSuccess { books ->
                    view?.apply {
                        cachedBooks = books
                        hideLoading()
                        showData(cachedBooks)
                    }
                }
                .onFailure { e ->
                    view?.apply {
                        hideLoading()
                        showError(e.message.toString())
                    }
                }
        }
    }

    fun incrementFirstIndex() {
        presenterScope.launch {
            if (cachedBooks.isEmpty()) {
                view?.showError("Cannot increment, somehow books list is empty")
            } else {
                val updatedBooks = cachedBooks.toMutableList().apply {
                    this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                }
                cachedBooks = updatedBooks
                view?.showData(updatedBooks)
            }
        }
    }
}