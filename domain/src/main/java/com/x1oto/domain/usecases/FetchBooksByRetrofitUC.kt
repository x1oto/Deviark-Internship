package com.x1oto.domain.usecases

import com.x1oto.domain.repositories.BookRepository
import javax.inject.Inject

class FetchBooksByRetrofitUC @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke() {
        repository.testRetrofitRequests()
    }
}