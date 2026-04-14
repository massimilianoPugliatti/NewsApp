package com.example.newsapp.feature.news.domain.usecase

import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(url: String): Flow<Article?> {
        return repository.getArticleByUrl(url)
    }
}