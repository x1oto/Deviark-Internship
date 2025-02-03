package com.x1oto.domain.usecases

import com.x1oto.domain.model.Book
import com.x1oto.domain.repositories.BookRepository

class GetRandomBookUC(private val bookRepository: BookRepository) {
    suspend operator fun invoke(): Book {
        return bookRepository.getRandomBook()
    }
}
