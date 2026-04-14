package com.example.newsapp.feature.news.presentation.details

import com.example.newsapp.feature.news.domain.model.Article

sealed interface NewsDetailsUiState {
    data object Loading : NewsDetailsUiState
    data class Success(val article: Article) : NewsDetailsUiState
    data class Error(val message: String) : NewsDetailsUiState
}