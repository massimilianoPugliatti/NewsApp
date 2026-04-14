package com.example.newsapp.core.di

import com.example.newsapp.feature.news.data.repository.NewsRepositoryImpl
import com.example.newsapp.feature.news.data.repository.local.LocalDataSource
import com.example.newsapp.feature.news.data.repository.remote.RemoteDataSource
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): NewsRepository =
        NewsRepositoryImpl(localDataSource, remoteDataSource)
}
