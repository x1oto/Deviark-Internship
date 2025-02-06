package com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.bookinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x1oto.domain.model.Book
import com.x1oto.domain.usecases.FetchBookByIdUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val fetchBookByIdUC: FetchBookByIdUC
) : ViewModel() {

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun fetchBookById(id: Long) = viewModelScope.launch {
        _loadingLiveData.value = true
        fetchBookByIdUC(id)
            .onSuccess { book ->
                _loadingLiveData.value = false
                _bookLiveData.value = book
            }
            .onFailure { e ->
                _loadingLiveData.value = false
                _errorSharedFlow.emit(e.message.toString())
            }
    }

}