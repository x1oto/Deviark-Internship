package com.x1oto.domain.usecases

import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class DeleteBooksUC @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(ids: List<Long>) {
        bookRepository.deleteBooks(ids)
    }
}