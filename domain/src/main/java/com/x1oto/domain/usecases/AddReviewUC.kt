package com.x1oto.domain.usecases

import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class AddReviewUC @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(id: Long, nickname: String, rating: Double, text: String) {
        bookRepository.addReview(id, nickname, rating, text)
    }
}