package com.x1oto.deviark_intership_1_1

import android.util.Log
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.x1oto.deviark_intership_1_1.models.Book
import com.x1oto.deviark_intership_1_1.models.Genre
import com.x1oto.deviark_intership_1_1.models.State
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
                    // Test any fun here, using Database. syntax

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
