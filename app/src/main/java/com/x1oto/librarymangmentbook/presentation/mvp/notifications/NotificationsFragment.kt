package com.x1oto.librarymangmentbook.presentation.mvp.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.databinding.FragmentNotificationsBinding
import com.x1oto.librarymangmentbook.presentation.BookAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment(), NotificationView {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter

    private val presenter = NotificationPresenter(this)

    private var searchJob: Job? = null

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
        setupListeners()
    }

    private fun initRecyclerView() {
        bookAdapter = BookAdapter()
        binding.booksRv.adapter = bookAdapter
    }

    private fun setupListeners() {
        binding.searchBt.setOnClickListener {
            val textFromEditText = binding.queryEt.text.toString()

            searchJob?.cancel()

            searchJob = lifecycleScope.launch {
                presenter.getBookByQuery(textFromEditText)
            }
        }
    }

    override fun showLoading() {
        binding.booksRv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.booksRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun showData(books: List<Book>) {
        if (books.isEmpty()) {
            showError("We do not have such books!")
        } else {
            bookAdapter.setBooks(books)
        }
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}