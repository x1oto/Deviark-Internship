package com.x1oto.librarymangmentbook.presentation.mvvm.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.x1oto.librarymangmentbook.databinding.FragmentDashboardBinding
import com.x1oto.librarymangmentbook.presentation.BookAdapter
import com.x1oto.librarymangmentbook.presentation.mvi.home.HomeEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var bookAdapter: BookAdapter

    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            bookAdapter.setBooks(books)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.booksRv.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
