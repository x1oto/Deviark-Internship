package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow

class GetLessPopularBooksUC(private val bookRepository: BookRepository) {
    operator fun invoke(term: Long = 1500): Flow<Result<List<Book>>> {
        return bookRepository.getLessPopularBooks(term)
    }
}
