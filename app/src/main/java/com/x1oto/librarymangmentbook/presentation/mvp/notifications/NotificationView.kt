package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import com.x1oto.librarymangmentbook.data.Book

interface NotificationView {
    fun showLoading()
    fun hideLoading()
    fun showData(books: List<Book>)
    fun showError(error: String)
}