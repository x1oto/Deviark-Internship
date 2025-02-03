package com.x1oto.data

import com.x1oto.data.Database.books
import com.x1oto.data.constants.Constants.FETCH_BOOKS_ERROR
import com.x1oto.data.constants.Constants.FETCH_POPULAR_BOOKS_ERROR
import com.x1oto.data.constants.Constants.UPLOAD_BOOKS_ERROR
import com.x1oto.domain.repositories.BookRepository
import com.x1oto.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class BookRepositoryImpl @Inject constructor() : BookRepository {

    override suspend fun getRandomBook(): Book {
        delay(300)
        return Database.randomBooks.random()
    }

    override fun fetchBooks(delayMs: Long): Flow<Result<List<Book>>> = flow {
        val chance = generateRandomInt()
        delay(delayMs)
        when (chance) {
            in 0..80 -> {
                emit(Result.success(books))
            }
            else -> {
                emit(Result.failure(Exception(FETCH_BOOKS_ERROR)))
            }
        }
    }.flowOn(Dispatchers.Default)


    override fun getMostPopularBooks(term: Long): Flow<Result<List<Book>>> = flow {
        val chance = generateRandomInt()
        delay(term)
        when (chance) {
            in 0..80 -> {
                emit(Result.success(books.sortedByDescending { it.borrowCount }))
            }
            else -> {
                emit(Result.failure(Exception(FETCH_POPULAR_BOOKS_ERROR)))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun getLessPopularBooks(term: Long): Flow<Result<List<Book>>> = flow {
        val chance = generateRandomInt()
        delay(term)
        when (chance) {
            in 0..80 -> {
                emit(Result.success(books.sortedBy { it.borrowCount }))
            }
            else -> {
                emit(Result.failure(Exception(FETCH_POPULAR_BOOKS_ERROR)))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun searchBooksByQuery(query: String, term: Long): Flow<Result<List<Book>>> = flow {
        val chance = generateRandomInt()
        delay(term)
        when (chance) {
            in 0..80 -> {
                emit(
                    Result.success(
                        books.filter {
                            it.title.contains(query, ignoreCase = true) ||
                                    it.genre.name.contains(query, ignoreCase = true)
                        }
                    )
                )
            }
            else -> {
                emit(Result.failure(Exception("Error searching books by query")))
            }
        }
    }.flowOn(Dispatchers.Default)


    override suspend fun updateBooks(
        updatedBooks: List<Book>,
        delayMs: Long
    ): Result<Boolean> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(delayMs)
            when (chance) {
                in 0..90 -> {
                    books = updatedBooks as MutableList<Book>
                    Result.success(true)
                }

                else -> {
                    Result.failure(Exception(UPLOAD_BOOKS_ERROR))
                }
            }
        }
    }

    private fun generateRandomInt() = Random.nextInt(until = 100)

}