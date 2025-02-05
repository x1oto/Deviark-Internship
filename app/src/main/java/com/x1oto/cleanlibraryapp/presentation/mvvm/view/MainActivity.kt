package com.x1oto.cleanlibraryapp.presentation.mvvm.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.x1oto.cleanlibraryapp.R
import com.x1oto.cleanlibraryapp.databinding.ActivityMainBinding
import com.x1oto.cleanlibraryapp.presentation.BookAdapter
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var bookAdapter: BookAdapter

    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        subscribeObservables()
        setOnClicks()
        initRecyclerView()

    }

    private fun setOnClicks() {
        binding.clearSortBt.setOnClickListener {
            viewModel.fetchBooks()
        }

        binding.sortDescBt.setOnClickListener {
            viewModel.getMostPopularBook()
        }

        binding.sortBt.setOnClickListener {
            viewModel.getLessPopularEvent()
        }

        binding.searchBt.setOnClickListener {
            query = binding.queryEt.text.toString()
            viewModel.getBookByQuery(query)
        }

        binding.addCountBt.setOnClickListener {
            viewModel.incrementFirstIndex()
        }

    }

    private fun initRecyclerView() {
        bookAdapter = BookAdapter()
        binding.booksRv.adapter = bookAdapter
    }

    private fun subscribeObservables() {
        viewModel.loadingLiveData.observe(this) {
            showLoading(it)

            binding.addBookBt.setOnClickListener {
                viewModel.saveTemporaryBooks()
            }
        }

        viewModel.booksLiveData.observe(this) { books ->
            viewModel.checkTemporaryBooks(books)
            bookAdapter.setBooks(books)

            binding.addBookBt.setOnClickListener {
                viewModel.addBook(books)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorSharedFlow.collectLatest {
                    showToast(it)

                    binding.addBookBt.setOnClickListener {
                        viewModel.saveTemporaryBooks()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.booksRv.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}