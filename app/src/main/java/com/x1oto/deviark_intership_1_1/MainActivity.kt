package com.x1oto.deviark_intership_1_1

import android.util.Log
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.x1oto.deviark_intership_1_1.models.Book
import com.x1oto.deviark_intership_1_1.models.State
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val state = MutableLiveData<State>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println("hhh main onCreate!")
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
                    // Test any fun here, using Database. syntax
                    paginationSimultaneouslyManyCall()

                }
            }
        }
    }

    // Upd: I have deleted hardcoded realizations for task 1.3-1.4
    // Search them at 1-3--1-4 branch.

    // Here's advanced realization of 1-3--1-4:
    // You can also find them at 1-3--1-4 branch:

    // 1.3-1.4
    fun parallel(vararg tasks: suspend () -> Unit) = lifecycleScope.launch {
        tasks.map { task ->
            async(Dispatchers.Default) {
                task()
            }
        }.awaitAll()
    }

    // 1.3-1.4
    fun sequentially(vararg tasks: suspend () -> Unit) = lifecycleScope.launch {
        for (task in tasks) {
            launch(Dispatchers.Default) {
                task()
            }.join()
        }
    }

    // 1.5 Два запити.
    private fun paginationSimultaneouslyTwoCall() = lifecycleScope.launch {
        // Запускаємо перший async, він працює, батьківський потік не блокуєтсья
        val page1 = async(Dispatchers.Default) {
            Database.getByPage(1)
        }
        // Запускаємо другий async, він працює, батьківський потік не блокуєтсья
        val page2 = async(Dispatchers.Default) {
            Database.getByPage(2)
        }

        // Тут за допомогою awaitAll() очікуємо на них
        println(awaitAll(page1, page2))
    }

    // 1.5
    private fun paginationSimultaneouslyManyCall() = lifecycleScope.launch {
        val deferred = mutableListOf<Deferred<List<Book>?>>()
        // Запускаємо 15 async, і додаємо їхні Deffered в ORDERED список, щоб зберегти послідовність
        repeat(15) { i ->
            deferred.add(async(Dispatchers.Default) {
                Database.getByPage(i + 1)
            })
        }
        // Накидуємо на ці deffered await, і після закінчення отримаємо послідовний список.
        // Ensure order is saved +
        val result = deferred.awaitAll()
    }

    private fun getBooks() {
        state.value = State.Loading
        lifecycleScope.launch(Dispatchers.IO) {
            Database.fetchBooks()
                .onSuccess { books ->
                    state.postValue(State.Success(books))
                }
                .onFailure { e ->
                    state.postValue(State.Error(e.toString()))
                }
        }
    }

}