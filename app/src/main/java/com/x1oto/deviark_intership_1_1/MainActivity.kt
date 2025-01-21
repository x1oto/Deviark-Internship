package com.x1oto.deviark_intership_1_1

import android.util.Log
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val state = MutableLiveData<State>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        getBooks()
    }

    private fun setupUI() {
        state.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    Log.d("Deviark", "An error occurred! Here it is: ${state.message}")
                }
                State.Loading -> {
                    Log.d("Deviark", "Loading...")
                }
                is State.Success -> {
                    Log.d("Deviark", "Fetched successfully: ${state.data}")

                    val newBook = Book(11, "The Catcher in the Rye", Genre.CLASSIC, 1951)
                    Database.addBook(newBook)
                    Log.d("Deviark", "Added book: ${newBook.title}")

                    val searchResults = Database.searchBooks("Classic")
                    Log.d("Deviark", "Search results for 'Classic':")
                    searchResults.forEach { Log.d("Deviark", "- ${it.title}") }

                    val borrowed = Database.borrowBook(1)
                    Log.d("Deviark", if (borrowed) "Successfully borrowed book with ID 1" else "Failed to borrow book with ID 1")

                    val returnBook = Book(1, "Kotlin in Action", Genre.PROGRAMMING, 2017)
                    Database.returnBook(returnBook)
                    Log.d("Deviark", "Returned book ID: ${returnBook.id}")
                }
            }
        }
    }

    private fun getBooks() {
        state.value = State.Loading
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getBooks()
                .onSuccess { books ->
                    state.postValue(State.Success(books))
                }
                .onFailure { e ->
                    state.postValue(State.Error(e.toString()))
                }
        }
    }
}

sealed interface State {
    object Loading: State
    class Success(val data: List<Book>): State
    class Error(val message: String): State
}