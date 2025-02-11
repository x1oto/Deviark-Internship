package com.x1oto.cleanlibraryapp.presentation.mvvm.view.bookinfo

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
import androidx.navigation.fragment.navArgs
import com.x1oto.cleanlibraryapp.R
import com.x1oto.cleanlibraryapp.databinding.FragmentBookInfoBinding
import com.x1oto.cleanlibraryapp.presentation.mvvm.view.home.HomeFragmentDirections
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.bookinfo.BookInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookInfoFragment : Fragment() {

    private var _binding: FragmentBookInfoBinding? = null
    private val binding get() = _binding!!

    private val args: BookInfoFragmentArgs by navArgs()
    private val viewModel: BookInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.bookLiveData.value == null) {
            fetchBook()
        }
        subscribeToObservables()
        setListeners()
    }

    private fun setListeners() {
        binding.moveToEditBookBt.setOnClickListener {
            val action = BookInfoFragmentDirections.moveToEditBookFragment()
            findNavController().navigate(action)
        }

        binding.confirmationBt.setOnClickListener {
            val action = BookInfoFragmentDirections.moveToAreYouSureDialog()
            findNavController().navigate(action)
        }
    }

    private fun fetchBook() {
        viewModel.fetchBookById(args.bookId)
    }

    private fun subscribeToObservables() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.group.visibility = if(it) View.GONE else View.VISIBLE
        }

        viewModel.bookLiveData.observe(viewLifecycleOwner) { book ->

            binding.rateBt.setOnClickListener {
                val action = BookInfoFragmentDirections.actionBookInfoFragmentToReviewFragment(book.id)
                findNavController().navigate(action)
            }

            binding.run {
                idTextView.text = book.id.toString()
                titleTextView.text = book.title
                authorTextView.text = book.author
                genreTextView.text = book.genre.name
                yearTextView.text = book.year.toString()
                borrowCountTextView.text = book.borrowCount.toString()
                isBorrowedTextView.text = book.isBorrowed.toString()
                lastBorrowedTimestampTextView.text = book.lastBorrowedTimestamp?.toString() ?: "-"
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorSharedFlow.collectLatest {
                    showToast(it)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}