package com.example.newsapp.feature.news.domain.model

sealed class NewsError: Throwable() {
    class NetworkError : NewsError()
    class ServerError : NewsError()
    class NoResults : NewsError()
    class UnknownError : NewsError()
}