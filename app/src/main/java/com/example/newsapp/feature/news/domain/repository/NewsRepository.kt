package com.example.newsapp.feature.news.domain.repository

import com.example.newsapp.feature.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getArticlesByQuery(query: String): Flow<List<Article>>
    suspend fun searchNews(query: String)
    fun getArticleByUrl(url: String): Flow<Article?>
    suspend fun toggleFavorite(article: Article)
}