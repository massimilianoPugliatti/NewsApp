package com.example.newsapp

import com.example.newsapp.feature.news.data.repository.NewsRepositoryImpl
import com.example.newsapp.feature.news.data.repository.local.LocalDataSource
import com.example.newsapp.feature.news.data.repository.remote.RemoteDataSource
import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.model.NewsError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {
    private val localDataSource: LocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: RemoteDataSource = mockk()
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        repository = NewsRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `searchNews calls remote and then inserts into local`() = runTest {
        val mockArticles = listOf(mockk<Article>())
        coEvery { remoteDataSource.fetchNews(any()) } returns mockArticles

        repository.searchNews("android")

        coVerify(exactly = 1) { remoteDataSource.fetchNews("android") }
        coVerify(exactly = 1) { localDataSource.insertArticles(mockArticles) }
    }

    @Test(expected = NewsError.NetworkError::class)
    fun `searchNews throws NetworkError when IOException occurs`() = runTest {
        coEvery { remoteDataSource.fetchNews(any()) } throws java.io.IOException()

        repository.searchNews("android")
    }
}