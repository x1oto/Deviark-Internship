package com.x1oto.deviark_intership_1_1

import android.util.Log
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.x1oto.deviark_intership_1_1.models.State
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


                }
            }
        }
    }

    private fun parallelHardcodedUsingAsync() = lifecycleScope.launch {
        println("Async | Launch root | ${Thread.currentThread().name}")

        val summaryDeferred = async(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Async 1 | ${Thread.currentThread().name}")
            Database.generateBorrowSummaryReport()
            val endTime = System.currentTimeMillis()
            endTime - startTime
        }

        val popularDeferred = async(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Async 2 | ${Thread.currentThread().name}")
            Database.getMostPopularBooks()
            val endTime = System.currentTimeMillis()
            endTime - startTime
        }

        awaitAll(summaryDeferred, popularDeferred)
            .forEachIndexed { i, v -> println("Async | End time async ${i + 1}: $v") }
    }

    private fun parallelHardcodedUsingLaunch() = lifecycleScope.launch {
        println("Launch | Launch root | ${Thread.currentThread().name}")

        val summary = launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Launch 1 | ${Thread.currentThread().name}")
            Database.generateBorrowSummaryReport()
            val endTime = System.currentTimeMillis()
            println("Launch | End time launch 1: ${endTime - startTime}")
        }

        val popular = launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Launch 2 | ${Thread.currentThread().name}")
            Database.getMostPopularBooks()
            val endTime = System.currentTimeMillis()
            println("Launch | End time launch 2: ${endTime - startTime}")
        }
    }

    fun parallel(vararg tasks: suspend () -> Unit) = lifecycleScope.launch {
        tasks.map { task ->
            async(Dispatchers.Default) {
                task()
            }
        }.awaitAll()
    }

    private fun sequentiallyHardcoded() = lifecycleScope.launch {
        println("Launch | Launch root | ${Thread.currentThread().name}")

        val summary = launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Launch 1 | ${Thread.currentThread().name}")
            Database.generateBorrowSummaryReport()
            val endTime = System.currentTimeMillis()
            endTime - startTime
            println("Launch | End time launch 1: ${endTime - startTime}")
        }.join()

        val popular = launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            println("Launch 2 | ${Thread.currentThread().name}")
            Database.getMostPopularBooks()
            val endTime = System.currentTimeMillis()
            endTime - startTime
            println("Launch | End time launch 2: ${endTime - startTime}")
        }.join()

    }

    fun sequentially(vararg tasks: suspend () -> Unit) = lifecycleScope.launch {
        for (task in tasks) {
            launch(Dispatchers.Default) {
                task()
            }.join()
        }
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

    // Try to catch onPause.
    // Basically it triggers when foreign service trying to cover your app.
    // Like google assistant, call, permissions.
    override fun onPause() {
        super.onPause()
        println("lifecycle onPause()")
    }

    override fun onStop() {
        super.onStop()
        println("lifecycle onStop()")
    }

    // How to trigger activity without onCreate call?
    // launchMode
    // Flags
    // Day-Night mode switch
}