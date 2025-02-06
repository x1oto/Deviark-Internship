package com.x1oto.cleanlibraryapp.presentation.mvvm.view.bookinfo

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.x1oto.cleanlibraryapp.R
import com.x1oto.cleanlibraryapp.presentation.mvvm.viewmodel.bookinfo.BookInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookInfoFragment : Fragment() {

    companion object {
        fun newInstance() = BookInfoFragment()
    }

    private val viewModel: BookInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_info, container, false)
    }
}