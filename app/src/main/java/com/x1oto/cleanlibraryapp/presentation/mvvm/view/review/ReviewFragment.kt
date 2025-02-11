package com.x1oto.cleanlibraryapp.presentation.mvvm.view.review

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.x1oto.cleanlibraryapp.databinding.FragmentReviewBinding
import com.x1oto.cleanlibraryapp.presentation.adapters.ReviewAdapter
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.review.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val args: ReviewFragmentArgs by navArgs()
    private val viewModel: ReviewViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter

    lateinit var backPressedCallback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setupObservables()
        onBackPressed()
        fetchReviewsSummary()
    }

    private fun setupObservables() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviewsSummary ->
            reviewAdapter.setReviews(reviewsSummary.reviews)
            setupTextViews(averageRate = reviewsSummary.average.toString(), count = reviewsSummary.size)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorSharedFlow.collectLatest {
                    setupTextViews()
                }
            }
        }
    }

    private fun setupTextViews(averageRate: String = "-", count: Int = 0) {
        binding.run {
            rateTv.text = averageRate.toString()
            val reviewCount = resources.getQuantityString(com.x1oto.cleanlibraryapp.R.plurals.reviews_count, count, count)
            reviewsQuantityTv.text = reviewCount
        }
    }

    private fun onBackPressed() {
        backPressedCallback = object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }
        activity?.onBackPressedDispatcher?.addCallback(this, backPressedCallback)
    }

    private fun initRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.reviewRv.adapter = reviewAdapter
    }

    private fun fetchReviewsSummary() {
        viewModel.fetchReviewsSummary(args.bookId)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.reviewRv.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        backPressedCallback.remove()
    }
}
