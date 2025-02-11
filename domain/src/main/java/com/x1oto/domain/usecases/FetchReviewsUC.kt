package com.x1oto.domain.usecases

import com.x1oto.domain.model.ReviewSummary
import com.x1oto.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchReviewsUC @Inject constructor(private val repository: BookRepository) {
    operator fun invoke(id: Long): Flow<Result<ReviewSummary>> {
        return repository.fetchReviews(id, delayMs = 1500L)
    }
}