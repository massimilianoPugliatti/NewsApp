package com.example.newsapp.feature.news.data.repository.remote.dto

data class NewsResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)


