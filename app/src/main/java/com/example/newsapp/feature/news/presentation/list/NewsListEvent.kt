package com.example.newsapp.feature.news.presentation.list

import com.example.newsapp.feature.news.domain.model.Article

sealed class NewsListEvent {
    data class OnSearchQueryChanged(val query: String) : NewsListEvent()
    object OnSearchTriggered : NewsListEvent()
    data class OnFavoriteClick(val article: Article) : NewsListEvent()
    object OnRefresh : NewsListEvent()
}