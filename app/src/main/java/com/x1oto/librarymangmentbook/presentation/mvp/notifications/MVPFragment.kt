package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.databinding.FragmentNotificationsBinding
import com.x1oto.librarymangmentbook.presentation.BookAdapter
import kotlinx.coroutines.launch

class MVPFragment : Fragment(), MyView {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        Presenter.attachView(this)
        Presenter.fetchBooks(false)
        setupListeners()
    }

    private fun initRecyclerView() {
        bookAdapter = BookAdapter()
        binding.booksRv.adapter = bookAdapter
    }

    private fun setupListeners() {
        binding.clearSortBt.setOnClickListener {
            Presenter.fetchBooks(isLoading)
        }

        binding.searchBt.setOnClickListener {
            val textFromEditText = binding.queryEt.text.toString()
            Presenter.getBookByQuery(isLoading, textFromEditText)
        }

        binding.addCountBt.setOnClickListener {
            Presenter.incrementFirstIndex()
        }

        binding.sortDescBt.setOnClickListener {
            Presenter.getMostPopularBook(isLoading)
        }

        binding.sortBt.setOnClickListener {
            Presenter.getLessPopularEvent(isLoading)
        }

    }

    override fun showLoading() {
        showLoading(true)
        isLoading = true

        binding.addBookBt.setOnClickListener {
            Presenter.saveTemporaryBooks()
        }
    }

    override fun hideLoading() {
        showLoading(false)
        isLoading = false
    }

    override fun showData(books: List<Book>) {
        val updatedBooks = Presenter.checkTemporaryBooks(books)
        bookAdapter.setBooks(updatedBooks)

        binding.addBookBt.setOnClickListener {
            Presenter.addBook()
        }
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()

        binding.addBookBt.setOnClickListener {
            Presenter.saveTemporaryBooks()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.booksRv.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Presenter.detachView()
        _binding = null
    }
}