package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchBooksUC @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(): Flow<Result<List<Book>>> {
        return bookRepository.fetchBooks(delayMs = 1500)
    }
}

