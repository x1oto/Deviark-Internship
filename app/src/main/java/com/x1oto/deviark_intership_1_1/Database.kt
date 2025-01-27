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
import kotlin.random.nextLong

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
        Book(10, "Crime and Punishment", "Fyodor Dostoevsky", Genre.CLASSIC, 1866, 12),
        Book(11, "Effective Java", "Joshua Bloch", Genre.PROGRAMMING, 2018, 6),
        Book(12, "Clean Code", "Robert C. Martin", Genre.PROGRAMMING, 2008, 9),
        Book(13, "Algorithms to Live By", "Brian Christian and Tom Griffiths", Genre.ALGORITHMS, 2016, 4),
        Book(14, "The Fellowship of the Ring", "J.R.R. Tolkien", Genre.FANTASY, 1954, 10),
        Book(15, "Emma", "Jane Austen", Genre.CLASSIC, 1815, 5),
        Book(16, "Sense and Sensibility", "Jane Austen", Genre.CLASSIC, 1811, 4),
        Book(17, "1984", "George Orwell", Genre.DYSTOPIAN, 1949, 8),
        Book(18, "The Road", "Cormac McCarthy", Genre.DYSTOPIAN, 2006, 7),
        Book(19, "Meditations", "Marcus Aurelius", Genre.PHILOSOPHICAL, 180, 5),
        Book(20, "War and Peace", "Leo Tolstoy", Genre.CLASSIC, 1869, 13),
        Book(21, "Anna Karenina", "Leo Tolstoy", Genre.CLASSIC, 1877, 11),
        Book(22, "The Two Towers", "J.R.R. Tolkien", Genre.FANTASY, 1954, 10),
        Book(23, "The Return of the King", "J.R.R. Tolkien", Genre.FANTASY, 1955, 12),
        Book(24, "Les Misérables", "Victor Hugo", Genre.CLASSIC, 1862, 10),
        Book(25, "The Brothers Karamazov", "Fyodor Dostoevsky", Genre.CLASSIC, 1880, 14),
        Book(26, "Man's Search for Meaning", "Viktor E. Frankl", Genre.PHILOSOPHICAL, 1946, 9),
        Book(27, "The Catcher in the Rye", "J.D. Salinger", Genre.CLASSIC, 1951, 6),
        Book(28, "The Silmarillion", "J.R.R. Tolkien", Genre.FANTASY, 1977, 8),
        Book(29, "Inferno", "Dante Alighieri", Genre.PHILOSOPHICAL, 1320, 10),
        Book(30, "Algorithm Design Manual", "Steven S. Skiena", Genre.ALGORITHMS, 2008, 7)
    )


    // 1.5
    // Method that simulate logic of internet request.
    suspend fun getByPage(page: Int): List<Book>? {
        val random = Random.nextLong(from = 500, until = 5000)
        delay(random)
        val grouped = books.mapIndexed { index, item ->
            (index / 2) + 1 to item
        }.groupBy(
            { it.first },
            { it.second }
        )
        return grouped[page]
    }

    suspend fun fetchBooks(): Result<List<Book>> {
        return withContext(Dispatchers.IO) {
            val random = Random.nextInt(100)
            delay(2000)
            if (random in 0..100) {
                Result.success(books)
            } else {
                Result.failure(Exception(ERROR))
            }
        }
    }

    fun addNewBook(book: Book) {
        books.add(book)
    }

    fun searchBooksByQuery(query: String): List<Book> {
        return books.filter { it.title.contains(query, ignoreCase = true) || it.genre.name.contains(query, ignoreCase = true) }
    }

    fun borrowBookById(id: Long): Boolean {
        val book = books.find { it.id == id && it.isBorrowed.not() }
        book?.let {
            it.borrowCount++
            it.isBorrowed = true
            it.lastBorrowedTimestamp = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun returnBorrowedBook(id: Long): Boolean {
        val book = books.find { it.id == id && it.isBorrowed }
        book?.let {
            it.isBorrowed = false
            return true
        }
        return false
    }

    suspend fun groupBooksByGenre(): Map<Genre, List<Book>> {
        delay(2500)
        return books.groupBy { it.genre }
    }

    suspend fun sortBooksByCriteria(criteria: Criteria): List<Book> {
        delay(2500)
        val sortedByAvailability = books.sortedBy { it.isBorrowed }
        return when (criteria) {
            Criteria.TITLE -> sortedByAvailability.sortedBy { it.title }
            Criteria.YEAR -> sortedByAvailability.sortedBy { it.year }
            Criteria.GENRE -> sortedByAvailability.sortedBy { it.genre }
            Criteria.AUTHOR -> sortedByAvailability.sortedBy { it.author }
        }
    }

    suspend fun countBooksByCriteria(criteria: Criteria): Map<out Any, Int> {
        delay(2500)
        return when (criteria) {
            Criteria.TITLE -> books.groupBy { it.title }.mapValues { (_, list) -> list.size }
            Criteria.YEAR -> books.groupBy { it.year }.mapValues { (_, list) -> list.size }
            Criteria.GENRE -> books.groupBy { it.genre }.mapValues { (_, list) -> list.size }
            Criteria.AUTHOR -> books.groupBy { it.author }.mapValues { (_, list) -> list.size }
        }
    }

    suspend fun getMostPopularBooks(): List<Book> {
        delay(2500)
        return books.sortedByDescending { it.borrowCount }
    }

    suspend fun getAvailableBooks(): List<Book> {
        delay(2500)
        return books.filter { it.isBorrowed.not() }
    }

    suspend fun getBorrowedBooks(): List<Book> {
        delay(2500)
        return books.filter { it.isBorrowed }
    }

    suspend fun filterBooksByAuthor(author: String): List<Book> {
        delay(2500)
        return books.filter { it.author == author }
    }

    suspend fun fetchUniqueAuthors(): Set<String> {
        delay(2500)
        return books.map { it.author }.toSet()
    }

    suspend fun generateBorrowSummaryReport(): Map<Genre, Int> {
        delay(2500)
        return groupBooksByGenre().mapValues { (_, books) ->
            books.sumOf { it.borrowCount }
        }
    }

    suspend fun getTrendingAuthors(durationInMillis: Long = 5 * 60 * 1000): List<String> {
        delay(2500)
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

