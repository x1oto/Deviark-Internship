package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class UpdateBooksUC @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(updatedBooks: List<Book>, delayMs: Long = 2500): Result<Boolean> {
        return bookRepository.updateBooks(updatedBooks, delayMs)
    }
}
