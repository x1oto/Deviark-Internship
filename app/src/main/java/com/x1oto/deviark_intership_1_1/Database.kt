package com.x1oto.deviark_intership_1_1

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.random.Random

enum class Genre {
    PROGRAMMING,
    ALGORITHMS,
    FANTASY,
    CLASSIC,
    DYSTOPIAN,
    PHILOSOPHICAL
}

data class Book(
    val id: Long,
    val title: String,
    val genre: Genre,
    val year: Short
)

object Database {

    const val ERROR = "Oh, damn! Totally unexpected error with 30% probability."

    private val books = mutableListOf(
        Book(1, "Kotlin in Action", Genre.PROGRAMMING, 2017),
        Book(2, "Kotlin Head First", Genre.PROGRAMMING, 2021),
        Book(3, "Grokking Algorithms", Genre.ALGORITHMS, 2016),
        Book(4, "The Hobbit", Genre.FANTASY, 1937),
        Book(5, "Pride and Prejudice", Genre.CLASSIC, 1813),
        Book(6, "To Kill a Mockingbird", Genre.CLASSIC, 1960),
        Book(7, "The Great Gatsby", Genre.CLASSIC, 1925),
        Book(8, "Brave New World", Genre.DYSTOPIAN, 1932),
        Book(9, "The Alchemist", Genre.PHILOSOPHICAL, 1988),
        Book(10, "Crime and Punishment", Genre.CLASSIC, 1866)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getBooks(): Result<List<Book>> {
        delay(1500)
        return suspendCancellableCoroutine { continuation ->
            val random = Random.nextInt(100)

            if(random in 0..70) {
                continuation.resume(Result.success(books)) {}
            } else {
                continuation.resume(Result.failure(Exception(ERROR))) {}
            }
        }
    }

    fun addBook(book: Book) {
        books.add(book)
    }

    fun searchBooks(query: String): List<Book> {
        return books.filter { it.title.contains(query, ignoreCase = true) || it.genre.name.contains(query, ignoreCase = true) }
    }

    fun borrowBook(bookId: Long): Boolean {
        val book = books.find { it.id == bookId }
        return if (book != null) {
            books.remove(book)
            true
        } else {
            false
        }
    }

    fun returnBook(book: Book) {
        books.add(book)
    }
}

