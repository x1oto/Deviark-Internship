package com.x1oto.librarymangmentbook.presentation.mvi.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.x1oto.librarymangmentbook.data.Book
import com.x1oto.librarymangmentbook.data.Database
import com.x1oto.librarymangmentbook.databinding.FragmentHomeBinding
import com.x1oto.librarymangmentbook.presentation.BookAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var bookAdapter: BookAdapter

    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservables()
        setupListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        bookAdapter = BookAdapter()
        binding.booksRv.adapter = bookAdapter
    }

    private fun setupListeners() {
        binding.clearSortBt.setOnClickListener {
            viewModel.send(HomeEvent.ClearSortingEvent)
        }

        binding.sortDescBt.setOnClickListener {
            viewModel.send(HomeEvent.GetMostPopularEvent)
        }

        binding.sortBt.setOnClickListener {
            viewModel.send(HomeEvent.GetLessPopularEvent)
        }

        binding.searchBt.setOnClickListener {
            query = binding.queryEt.text.toString()
            viewModel.send(HomeEvent.SearchEvent(query))
        }

        binding.addCountBt.setOnClickListener {
            viewModel.send(HomeEvent.IncrementCountEvent)
        }
    }

    private fun subscribeObservables() {
        viewModel.homeLiveData.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: HomeState) {
        when (state) {
            is HomeState.Error -> {
                showLoading(false)
                showToast(state.message)

                binding.addBookBt.setOnClickListener {
                    viewModel.send(HomeEvent.SaveTemporaryBooksEvent)
                }
            }

            HomeState.Loading -> {
                showLoading(true)

                binding.addBookBt.setOnClickListener {
                    viewModel.send(HomeEvent.SaveTemporaryBooksEvent)
                }
            }

            is HomeState.Data -> {
                showLoading(false)
                viewModel.send(HomeEvent.CheckTemporaryBooksStatusEvent(state.books))
                updateBooks(state.books)

                binding.addBookBt.setOnClickListener {
                    viewModel.send(HomeEvent.AddBookEvent(state.books))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.booksRv.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun updateBooks(books: List<Book>) {
        bookAdapter.setBooks(books)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
