package com.example.newsapp.feature.news.presentation.list

sealed class NewsListEffect {
        data class ShowSnackbar(val message: String) : NewsListEffect()
        object ScrollToTop : NewsListEffect()
    }