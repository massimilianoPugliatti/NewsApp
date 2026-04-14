package com.example.newsapp

import com.example.newsapp.feature.news.domain.model.NewsError
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import com.example.newsapp.feature.news.domain.usecase.SearchNewsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchNewsUseCaseTest {
    private val repository: NewsRepository = mockk()
    private lateinit var searchNewsUseCase: SearchNewsUseCase

    @Before
    fun setup() {
        searchNewsUseCase = SearchNewsUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository completes`() = runTest {
        coEvery { repository.searchNews(any()) } just Runs

        val result = searchNewsUseCase("android")

        assert(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when repository throws NewsError`() = runTest {
        val error = NewsError.NetworkError()
        coEvery { repository.searchNews(any()) } throws error

        val result = searchNewsUseCase("android")

        assert(result.isFailure)
        assert(result.exceptionOrNull() is NewsError.NetworkError)
    }
}