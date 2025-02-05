package com.x1oto.cleanlibraryapp.di

import com.x1oto.domain.repositories.BookRepository
import com.x1oto.domain.usecases.FetchBooksUC
import com.x1oto.domain.usecases.GetLessPopularBooksUC
import com.x1oto.domain.usecases.GetMostPopularBooksUC
import com.x1oto.domain.usecases.GetRandomBookUC
import com.x1oto.domain.usecases.SearchBooksByQueryUC
import com.x1oto.domain.usecases.UpdateBooksUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetRandomBookUC(bookRepository: BookRepository) = GetRandomBookUC(bookRepository)

    @Provides
    fun provideFetchBooksUC(bookRepository: BookRepository) = FetchBooksUC(bookRepository)

//    @Provides
//    fun provideGetMostPopularBooksUC(bookRepository: BookRepository) = GetMostPopularBooksUC(bookRepository)

    @Provides
    fun provideGetLessPopularBooksUC(bookRepository: BookRepository) = GetLessPopularBooksUC(bookRepository)

    @Provides
    fun provideSearchBooksByQueryUC(bookRepository: BookRepository) = SearchBooksByQueryUC(bookRepository)

    @Provides
    fun provideUpdateBooksUC(bookRepository: BookRepository) = UpdateBooksUC(bookRepository)
}