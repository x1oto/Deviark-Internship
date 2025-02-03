package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow

class SearchBooksByQueryUC(private val bookRepository: BookRepository) {
    operator fun invoke(query: String, term: Long = 1500): Flow<Result<List<Book>>> {
        return bookRepository.searchBooksByQuery(query, term)
    }
}
