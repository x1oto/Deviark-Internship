package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class FetchBookByIdUC @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(id: Long): Result<Book> {
         return bookRepository.fetchBookById(id)
    }
}