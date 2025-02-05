package com.x1oto.cleanlibraryapp.di

import com.x1oto.data.BookRepositoryImpl
import com.x1oto.domain.repositories.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// Binds - works ONLY with interfaces!
@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    @Singleton
    abstract fun provideBookRepositoryImpl(bookRepositoryImpl: BookRepositoryImpl): BookRepository
}