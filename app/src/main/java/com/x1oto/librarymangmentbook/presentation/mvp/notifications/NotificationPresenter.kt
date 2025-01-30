package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database

class NotificationPresenter(private val view: NotificationView) {

    suspend fun getBookByQuery(query: String) {
        view.showLoading()
        Database.searchBooksByQuery(query)
            .onSuccess { books ->
                view.hideLoading()
                view.showData(books = books)
            }
            .onFailure { e ->
                view.hideLoading()
                view.showError(e.message.toString())
            }
    }

}