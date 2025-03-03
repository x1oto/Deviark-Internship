package com.x1oto.cleanlibraryapp.presentation.mvvm.view.home

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.x1oto.cleanlibraryapp.databinding.DialogModalReportBottomSheetBinding
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.home.ReportBottomSheetViewModel

class ReportBottomSheet(private val bookId: Long) : BottomSheetDialogFragment() {

    private var _binding: DialogModalReportBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportBottomSheetViewModel by activityViewModels()

    private var reviewText: String? = null

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.linearLayout.post {
            val heightPx = binding.linearLayout.height
            behavior.peekHeight = convertPxToDp(heightPx)
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setupAnimation(slideOffset)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogModalReportBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardViewFirstFastReview.setOnClickListener {
            viewModel.commitReview(bookId, text = "Interesting book, recommend!")
            dismiss()
        }

        binding.cardViewSecondFastReview.setOnClickListener {
            viewModel.commitReview(bookId, text = "Boring")
            dismiss()
        }

        binding.buttonSubmit.setOnClickListener {
            reviewText = binding.editTextCustomReason.text.toString()
            viewModel.commitReview(bookId, text = reviewText!!)
            dismiss()
        }
    }

    private fun setupAnimation(slideOffset: Float) {
        binding.run {
            textViewExpandLabel.alpha = (1.0F - (slideOffset * 2))
            textViewWriteLabel.alpha = -(1.0F - (slideOffset * 2))
            inputLayoutCustomReason.alpha = -(1.0F - (slideOffset * 2))
            buttonSubmit.alpha = -(1.0F - (slideOffset * 2))
        }
    }

    private fun convertPxToDp(px: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}