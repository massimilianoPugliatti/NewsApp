package com.example.newsapp.feature.news.presentation.details

import com.example.newsapp.feature.news.domain.model.Article

sealed class NewsDetailsEvent {
    data class OnFavoriteClick(val article: Article) : NewsDetailsEvent()
}