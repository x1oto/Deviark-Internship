package com.x1oto.librarymangmentbook.data

import com.x1oto.librarymangmentbook.data.Constants.FETCH_BOOKS_ERROR
import com.x1oto.librarymangmentbook.data.Constants.FETCH_POPULAR_BOOKS_ERROR
import com.x1oto.librarymangmentbook.data.Constants.UPLOAD_BOOKS_ERROR
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

object Database {

    private var books = mutableListOf(
        Book(
            1,
            "Kotlin in Action",
            "Dmitry Jemerov and Svetlana Isakova",
            Genre.PROGRAMMING,
            2017,
            5
        ),
        Book(
            2,
            "Kotlin Head First",
            "Dawn Griffiths and David Griffiths",
            Genre.PROGRAMMING,
            2021,
            2
        ),
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
        Book(
            13,
            "Algorithms to Live By",
            "Brian Christian and Tom Griffiths",
            Genre.ALGORITHMS,
            2016,
            4
        ),
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

    // List of random books
    private val randomBooks = mutableListOf(
        Book(1, "THE GREAT GATSBY", "F. Scott Fitzgerald", Genre.CLASSIC, 1925, 8),
        Book(2, "THE CATCHER IN THE RYE", "J.D. Salinger", Genre.CLASSIC, 1951, 6),
        Book(3, "ALGORITHMS UNLOCKED", "Thomas H. Cormen", Genre.ALGORITHMS, 2013, 5),
        Book(4, "THE DARK TOWER", "Stephen King", Genre.FANTASY, 1982, 7),
        Book(5, "THE ART OF COMPUTER PROGRAMMING", "Donald E. Knuth", Genre.PROGRAMMING, 1968, 9),
        Book(6, "THE MATRIX AND THE MIND", "Robert M. Pirsig", Genre.PHILOSOPHICAL, 2006, 4),
        Book(7, "MEDITATIONS", "Marcus Aurelius", Genre.PHILOSOPHICAL, 180, 5),
        Book(8, "THE ROAD LESS TRAVELLED", "M. Scott Peck", Genre.PHILOSOPHICAL, 1978, 8),
        Book(9, "THE LORD OF THE RINGS", "J.R.R. Tolkien", Genre.FANTASY, 1954, 12)
    )

    suspend fun getRandomBook(): Book {
        delay(300)
        return randomBooks.random()
    }


    // Imitation of query
    suspend fun fetchBooks(term: Long = 1500): Result<List<Book>> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(term)
            when (chance) {
                in 0..80 -> {
                    Result.success(books)
                }

                else -> {
                    Result.failure(Exception(FETCH_BOOKS_ERROR))
                }
            }
        }
    }

    // Backend upload imitation
    suspend fun updateBooks(updatedBooks: MutableList<Book>, term: Long = 2500): Result<Boolean> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(term)
            when (chance) {
                in 0..90 -> {
                    books = updatedBooks
                    Result.success(true)
                }

                else -> {
                    Result.failure(Exception(UPLOAD_BOOKS_ERROR))
                }
            }
        }
    }

    suspend fun getMostPopularBooks(term: Long = 2500): Result<List<Book>> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(term)
            when (chance) {
                in 0..80 -> {
                    Result.success(books.sortedByDescending { it.borrowCount })
                }

                else -> {
                    Result.failure(Exception(FETCH_POPULAR_BOOKS_ERROR))
                }
            }

        }
    }

    suspend fun getLessPopularBooks(term: Long = 2500): Result<List<Book>> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(term)
            when (chance) {
                in 0..80 -> {
                    Result.success(books.sortedBy { it.borrowCount })
                }

                else -> {
                    Result.failure(Exception(FETCH_POPULAR_BOOKS_ERROR))
                }
            }

        }
    }

    suspend fun searchBooksByQuery(query: String, term: Long = 2000): Result<List<Book>> {
        return withContext(Dispatchers.Default) {
            val chance = generateRandomInt()
            delay(term)
            when (chance) {
                in 0..80 -> {
                    Result.success(
                        books.filter {
                            it.title.contains(query, ignoreCase = true) ||
                                    it.genre.name.contains(query, ignoreCase = true)
                        }
                    )
                }

                else -> {
                    Result.failure(Exception("Error searching books by query"))
                }
            }
        }
    }


    private fun generateRandomInt() = Random.nextInt(until = 100)
}

