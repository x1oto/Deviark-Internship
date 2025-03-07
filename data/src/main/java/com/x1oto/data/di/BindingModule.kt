package com.x1oto.data.di

import com.x1oto.data.BookRepositoryImpl
import com.x1oto.domain.repositories.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {
    @Binds
    abstract fun provideBookRepositoryImpl(bookRepositoryImpl: BookRepositoryImpl): BookRepository
}