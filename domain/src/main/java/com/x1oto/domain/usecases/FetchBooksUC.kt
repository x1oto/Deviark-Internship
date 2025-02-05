package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow

class FetchBooksUC(private val bookRepository: BookRepository) {
    private val delayMs: Long = 1500
    operator fun invoke(): Flow<Result<List<Book>>> {
        return bookRepository.fetchBooks(delayMs)
    }
}

