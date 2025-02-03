package com.x1oto.domain.repositories

import com.x1oto.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getRandomBook(): Book
    fun fetchBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun getMostPopularBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun getLessPopularBooks(delayMs: Long): Flow<Result<List<Book>>>
    fun searchBooksByQuery(query: String, delayMs: Long): Flow<Result<List<Book>>>
    suspend fun updateBooks(updatedBooks: List<Book>, delayMs: Long): Result<Boolean>
}
