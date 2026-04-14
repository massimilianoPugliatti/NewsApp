package com.example.newsapp.feature.news.presentation.list

import com.example.newsapp.feature.news.domain.model.Article

sealed class NewsListUiState {
    object Loading : NewsListUiState()
    data class Success(val articles: List<Article>) : NewsListUiState()
    data class Error(val message: String) : NewsListUiState()
}