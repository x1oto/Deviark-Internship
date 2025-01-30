package com.x1oto.librarymangmentbook.presentation.mvvm.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.x1oto.librarymangmentbook.databinding.FragmentDashboardBinding
import com.x1oto.librarymangmentbook.presentation.BookAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var bookAdapter: BookAdapter

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
        binding.floatingActionButton.setOnClickListener {
            viewModel.fetchBooks()
        }

        binding.plusFab.setOnClickListener {
            viewModel.incrementFirstIndex()
        }

        binding.minusFab.setOnClickListener {
            viewModel.decrementFirstIndex()
        }
    }

    private fun subscribeObservables() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.booksRv.visibility = View.INVISIBLE
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.books.observe(viewLifecycleOwner) { books ->
            binding.booksRv.visibility = View.VISIBLE
            bookAdapter.setBooks(books)
        }

        lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.error.collectLatest {
                   Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
               }
           }
        }
    }

    private fun initRecyclerView() {
        bookAdapter = BookAdapter()
        binding.booksRv.adapter = bookAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
