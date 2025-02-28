package com.x1oto.cleanlibraryapp.presentation.mvvm.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.x1oto.cleanlibraryapp.databinding.DialogModalReportBottomSheetBinding

class ReportBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogModalReportBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.peekHeight = 650

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.textView3.alpha = (1.0F - (slideOffset * 2))
                binding.textView10.alpha = -(1.0F - (slideOffset * 2))
                binding.textView7.alpha = -(1.0F - (slideOffset * 4))
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
}