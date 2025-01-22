package com.x1oto.deviark_intership_1_1

import android.util.Log
import com.x1oto.deviark_intership_1_1.Constants.ERROR
import com.x1oto.deviark_intership_1_1.models.Book
import com.x1oto.deviark_intership_1_1.models.Criteria
import com.x1oto.deviark_intership_1_1.models.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.random.Random

object Database {

    private val books = mutableListOf(
        Book(1, "Kotlin in Action", "Dmitry Jemerov and Svetlana Isakova", Genre.PROGRAMMING, 2017, 5),
        Book(2, "Kotlin Head First", "Dawn Griffiths and David Griffiths", Genre.PROGRAMMING, 2021, 2),
        Book(3, "Grokking Algorithms", "Aditya Bhargava", Genre.ALGORITHMS, 2016, 3),
        Book(4, "The Hobbit", "J.R.R. Tolkien", Genre.FANTASY, 1937, 7),
        Book(5, "Pride and Prejudice", "Jane Austen", Genre.CLASSIC, 1813, 4),
        Book(6, "To Kill a Mockingbird", "Harper Lee", Genre.CLASSIC, 1960, 6),
        Book(7, "The Great Gatsby", "F. Scott Fitzgerald", Genre.CLASSIC, 1925, 8),
        Book(8, "Brave New World", "Aldous Huxley", Genre.DYSTOPIAN, 1932, 5),
        Book(9, "The Alchemist", "Paulo Coelho", Genre.PHILOSOPHICAL, 1988, 9),
        Book(10, "Crime and Punishment", "Fyodor Dostoevsky", Genre.CLASSIC, 1866, 12)
    )

    // Deleted suspendCancelableCoroutine. Reason: Overkill
    // Instead using delay() with return withContext.
    suspend fun getBooks(): Result<List<Book>> {
        return withContext(Dispatchers.IO) {
            val random = Random.nextInt(100)
            delay(2000)
            if(random in 0..70) {
                Result.success(books)
            } else {
                Result.failure(Exception(ERROR))
            }
        }
    }

    fun addBook(book: Book) {
        books.add(book)
    }

    fun searchBooks(query: String): List<Book> {
        return books.filter { it.title.contains(query, ignoreCase = true) || it.genre.name.contains(query, ignoreCase = true) }
    }

    fun borrowBook(id: Long): Boolean {
        val book = books.find { it.id == id && it.isBorrowed.not() }
        book?.let {
            it.borrowCount++
            it.isBorrowed = true
            it.lastBorrowedTimestamp = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun returnBook(id: Long): Boolean {
        val book = books.find { it.id == id && it.isBorrowed }
        book?.let {
            it.isBorrowed = false
            return true
        }
        return false
    }

    // Group books by Genre.
    fun groupBooks(): Map<Genre, List<Book>> {
        return books.groupBy { it.genre }
    }

    // Add sorting by title, release date, or price. For each sorting order show available first
    fun sortBy(criteria: Criteria): List<Book> {
        val sortedByAvailability = books.sortedBy { it.isBorrowed }
        return when(criteria) {
            Criteria.TITLE -> sortedByAvailability.sortedBy { it.title }
            Criteria.YEAR -> sortedByAvailability.sortedBy { it.year }
            Criteria.GENRE -> sortedByAvailability.sortedBy { it.genre }
            Criteria.AUTHOR -> sortedByAvailability.sortedBy { it.author }
        }
    }

    // Count total books by genre, by author
    fun countBy(criteria: Criteria): Map<out Any, Int> { // Compiler set out.
        return when(criteria) {
            Criteria.TITLE -> books.groupBy { it.title }.mapValues { (_, list) -> list.size }
            Criteria.YEAR -> books.groupBy { it.year }.mapValues { (_, list) -> list.size }
            Criteria.GENRE -> books.groupBy { it.genre }.mapValues { (_, list) -> list.size }
            Criteria.AUTHOR -> books.groupBy { it.author }.mapValues { (_, list) -> list.size }
        }
    }

    // show most popular(borrow count) books.
    fun sortPopular(): List<Book> {
        return books.sortedByDescending { it.borrowCount }
    }

    // Filter books by author or availability.
    fun filterByAvailability(): List<Book> {
        return books.filter { it.isBorrowed == false }
    }

    // Filter books by author or availability.
    private fun filterByNonAvailability(): List<Book> {
        return books.filter { it.isBorrowed == true }
    }

    // Filter books by author or availability.
    fun filterByAuthor(author: String): List<Book> {
        return books.filter { it.author == author }
    }

    // Extract all unique authors.
    fun uniqueAuthors(): Set<String> {
        return books.map { it.author }.toSet()
    }

    // Create a summary report for borrowed books count by genre.
    /**
     * Cool method that allows us, to change the values in existing map.
     * So basically, this fun might look like this.
     *
     *     fun getSummaryReport(): MutableMap<Genre, Int> {
     *         val grouped = groupBooks()
     *         val summary = mutableMapOf<Genre, Int>()
     *
     *         for((key, values) in grouped.entries) {
     *             summary.put(key, values.sumOf { it.borrowCount })
     *         }
     *
     *         return summary
     *     }
     *
     */
    fun getSummaryReport(): Map<Genre, Int> {
        return groupBooks().mapValues { (_, books) ->
            books.sumOf { it.borrowCount }
        }
    }

    // trending(last 5 min or another testable time) author
    fun trendingAuthors(durationInMillis: Long = 5 * 60 * 1000): List<String> {
        val currentTime = System.currentTimeMillis()
        val recentBorrowedBooks = books.filter { book ->
            book.lastBorrowedTimestamp?.let {
                currentTime - it <= durationInMillis
            } ?: false
        }
        return recentBorrowedBooks
            .groupBy { it.author }
            .entries
            .sortedByDescending { it.value.size }
            .map { it.key }
    }

}

