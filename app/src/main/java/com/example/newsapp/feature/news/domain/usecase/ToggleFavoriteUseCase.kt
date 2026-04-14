package com.example.newsapp.feature.news.domain.usecase

import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.toggleFavorite(article)
    }
}