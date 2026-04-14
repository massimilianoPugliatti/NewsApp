package com.example.newsapp

import app.cash.turbine.test
import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.model.NewsError
import com.example.newsapp.feature.news.domain.usecase.GetNewsUseCase
import com.example.newsapp.feature.news.domain.usecase.SearchNewsUseCase
import com.example.newsapp.feature.news.domain.usecase.ToggleFavoriteUseCase
import com.example.newsapp.feature.news.presentation.list.NewsListEvent
import com.example.newsapp.feature.news.presentation.list.NewsListUiState
import com.example.newsapp.feature.news.presentation.list.NewsListEffect
import com.example.newsapp.feature.news.presentation.list.NewsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val searchNewsUseCase: SearchNewsUseCase = mockk()
    private val getNewsUseCase: GetNewsUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { getNewsUseCase(any()) } returns flowOf(emptyList())
        coEvery { searchNewsUseCase(any()) } returns Result.success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `performSearch updates uiState to Success on usecase success`() = runTest {
        val mockArticles = listOf(mockk<Article>(relaxed = true))

        every { getNewsUseCase(any()) } returns flowOf(mockArticles)
        coEvery { searchNewsUseCase(any()) } returns Result.success(Unit)

        val viewModel = NewsViewModel(searchNewsUseCase, getNewsUseCase, toggleFavoriteUseCase)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(NewsListEvent.OnSearchTriggered)

            val state = awaitItem()
            assert(state is NewsListUiState.Success)
            assert((state as NewsListUiState.Success).articles == mockArticles)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `performSearch sends Snackbar event on NoResults error`() = runTest {
        coEvery { searchNewsUseCase(any()) } returns Result.failure(NewsError.NoResults())

        val viewModel = NewsViewModel(searchNewsUseCase, getNewsUseCase, toggleFavoriteUseCase)

        viewModel.uiEvent.test {
            viewModel.onEvent(NewsListEvent.OnSearchTriggered)

            val event = awaitItem()
            assert(event is NewsListEffect.ShowSnackbar)
            assert((event as NewsListEffect.ShowSnackbar).message == "Nessun risultato trovato.")

            cancelAndIgnoreRemainingEvents()
        }
    }
}