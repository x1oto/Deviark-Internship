package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class GetRandomBookUC @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(): Book {
        return bookRepository.getRandomBook()
    }
}
