package com.example.newsapp.feature.news.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.newsapp.feature.news.presentation.list.components.LoadingScreen
import com.example.newsapp.feature.news.presentation.list.components.NewsList
import com.example.newsapp.feature.news.presentation.list.components.OfflineEmptyList
import com.example.newsapp.feature.news.presentation.list.components.SearchBar

@Composable
fun NewsListScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onArticleClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is NewsListEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is NewsListEffect.ScrollToTop -> {
                    listState.scrollToItem(0)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onEvent(NewsListEvent.OnSearchQueryChanged(it)) },
                onSearch = { viewModel.onEvent(NewsListEvent.OnSearchTriggered) }
            )
            when (val state = uiState) {
                is NewsListUiState.Loading -> LoadingScreen()
                is NewsListUiState.Error -> OfflineEmptyList(onRetry = {
                    viewModel.onEvent(
                        NewsListEvent.OnRefresh
                    )
                })

                is NewsListUiState.Success -> NewsList(
                    articles = state.articles,
                    onArticleClick = onArticleClick,
                    onFavoriteClick = { viewModel.onEvent(NewsListEvent.OnFavoriteClick(it)) },
                    listState
                )
            }
        }
    }
}







