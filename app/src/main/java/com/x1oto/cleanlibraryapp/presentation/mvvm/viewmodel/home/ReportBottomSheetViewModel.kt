package com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.home

import androidx.lifecycle.ViewModel
import com.x1oto.domain.usecases.AddReviewUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportBottomSheetViewModel @Inject constructor(
    private val addReviewUC: AddReviewUC
) : ViewModel() {

    fun commitReview(id: Long, nickname: String = "Anonymous", rating: Double = 3.5, text: String) {
        addReviewUC(id, nickname, rating, text)
    }
}