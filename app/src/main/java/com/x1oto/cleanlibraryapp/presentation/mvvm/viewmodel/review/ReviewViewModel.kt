package com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.domain.model.ReviewSummary
import com.x1oto.domain.usecases.FetchReviewsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val fetchReviewsUC: FetchReviewsUC) :
    ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _reviewsLiveData = MutableLiveData<ReviewSummary>()
    val reviewsLiveData: LiveData<ReviewSummary> get() = _reviewsLiveData

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun fetchReviewsSummary(bookId: Long) {
        _loadingLiveData.value = true
        viewModelScope.launch {
            fetchReviewsUC(bookId).collectLatest { result ->
                result
                    .onSuccess { reviewSummary ->
                        _loadingLiveData.value = false
                        _reviewsLiveData.value = reviewSummary
                    }
                    .onFailure { e ->
                        _loadingLiveData.value = false
                        _errorSharedFlow.emit(e.message.toString())
                    }
            }
        }
    }
}