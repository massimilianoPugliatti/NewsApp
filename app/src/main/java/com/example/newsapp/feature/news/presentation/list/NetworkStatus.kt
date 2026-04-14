package com.example.newsapp.feature.news.presentation.list
sealed interface NetworkStatus {
    object Idle : NetworkStatus
    object Loading : NetworkStatus
    object Success : NetworkStatus
    data class Error(val message: String) : NetworkStatus
}