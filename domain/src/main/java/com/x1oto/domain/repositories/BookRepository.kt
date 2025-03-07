package com.x1oto.domain.repositories

import com.x1oto.domain.model.Book
import com.x1oto.domain.model.ReviewSummary
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    // Methods that we use in HomeFragment
    suspend fun getRandomBook(): Book
    fun fetchBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun getMostPopularBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun getLessPopularBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun searchBooksByQuery(query: String, delayMs: Long): Flow<Result<List<Book>>>
    suspend fun updateBooks(updatedBooks: List<Book>, delayMs: Long): Result<Boolean>
    fun deleteBooks(ids: List<Long>)
    // Methods that we use in BookInfoFragment
    suspend fun fetchBookById(id: Long): Result<Book>
    suspend fun testRetrofitRequests()

    // Methods that we use in Reviews
    fun fetchReviews(id: Long, delayMs: Long): Flow<Result<ReviewSummary>>
    fun addReview(id: Long, nickname: String, rating: Double, text: String)
}
