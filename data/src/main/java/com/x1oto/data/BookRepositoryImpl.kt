package com.x1oto.data

import com.x1oto.data.Database.books
import com.x1oto.data.Database.reviewsMap
import com.x1oto.data.constants.Constants.FETCH_BOOKS_ERROR
import com.x1oto.data.constants.Constants.FETCH_ONE_BOOK_ERROR
import com.x1oto.data.constants.Constants.FETCH_POPULAR_BOOKS_ERROR
import com.x1oto.data.constants.Constants.UPLOAD_BOOKS_ERROR
import com.x1oto.domain.repositories.BookRepository
import com.x1oto.domain.model.Book
import com.x1oto.data.model.ReviewDTO
import com.x1oto.domain.model.Review
import com.x1oto.domain.model.ReviewSummary
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

    override fun fetchReviews(id: Long, delayMs: Long): Flow<Result<ReviewSummary>> = flow {
        delay(delayMs)
        val specific = reviewsMap[id]

        if(specific != null) {
            emit(Result.success(specific.toSummary()))
        } else {
            emit(Result.failure(Exception("No reviews for this book.")))
        }

    }.flowOn(Dispatchers.Default)


    fun List<ReviewDTO>.toDomain(): List<Review> {
        return map { Review(it.nickname, it.rating, it.text) }
    }


    fun List<ReviewDTO>.toSummary(): ReviewSummary {
        val domainReviews = this.toDomain()
        val average = if (isNotEmpty()) sumOf { it.rating } / size else 0.0
        return ReviewSummary(domainReviews, size, average)
    }

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

    override suspend fun fetchBookById(id: Long): Result<Book> {
        delay(600)
        val foundedBook = books.find { it.id == id }
        return when(foundedBook) {
            null -> Result.failure(Exception(FETCH_ONE_BOOK_ERROR))
            else -> Result.success(foundedBook)
        }
    }

    private fun generateRandomInt() = Random.nextInt(until = 100)

}