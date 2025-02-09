package com.x1oto.cleanlibraryapp.presentation.mvvm.view.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.x1oto.cleanlibraryapp.databinding.FragmentHomeBinding
import com.x1oto.cleanlibraryapp.presentation.adapters.BookAdapter
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bookAdapter: BookAdapter
    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        bookAdapter = BookAdapter { bookId ->
            val action = HomeFragmentDirections.actionHomeFragmentToBookInfoFragment(bookId)
            findNavController().navigate(action)
        }
        binding.booksRv.adapter = bookAdapter
    }

    private fun subscribeObservables() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            showLoading(it)

            binding.addBookBt.setOnClickListener {
                viewModel.saveTemporaryBooks()
            }
        }

        viewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}