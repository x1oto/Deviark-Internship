package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Presenter {
    private val presenterScope = CoroutineScope(Dispatchers.Main)
    private var view: MyView? = null

    private var cacheBook: List<Book> = emptyList()
    private var isLoading = false

    init {
        fetchBooks(false)
    }

    fun attachView(view: MyView) {
        this.view = view
        if (isLoading) {
            view.showLoading()
        } else if (cacheBook.isNotEmpty()) {
            view.hideLoading()
            view.showData(cacheBook)
        }
    }

    fun detachView() {
        this.view = null
    }

    fun fetchBooks(isLoading: Boolean) {
        if(isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.fetchBooks()
                .onSuccess { books ->
                    view?.apply {
                        cacheBook = books
                        hideLoading()
                        showData(cacheBook)
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
        if(isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.getMostPopularBooks()
                .onSuccess { books ->
                    view?.apply {
                        cacheBook = books
                        hideLoading()
                        showData(cacheBook)
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
        if(isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.getLessPopularBooks()
                .onSuccess { books ->
                    view?.apply {
                        cacheBook = books
                        hideLoading()
                        showData(cacheBook)
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
        if(isLoading) return
        view?.showLoading()
        presenterScope.launch {
            Database.searchBooksByQuery(query)
                .onSuccess { books ->
                    view?.apply {
                        cacheBook = books
                        hideLoading()
                        showData(cacheBook)
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
            if (cacheBook.isEmpty()) {
                view?.showError("Cannot increment, somehow books list is empty")
            } else {
                val updatedBooks = cacheBook.toMutableList().apply {
                    this[0] = this[0].copy(borrowCount = this[0].borrowCount + 1)
                }
                cacheBook = updatedBooks
                view?.showData(updatedBooks)
            }
        }
    }
}