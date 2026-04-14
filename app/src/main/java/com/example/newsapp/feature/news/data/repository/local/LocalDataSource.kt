package com.example.newsapp.feature.news.data.repository.local

import com.example.newsapp.core.database.dao.ArticleDao
import com.example.newsapp.feature.news.domain.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val articleDao: ArticleDao
) {
    fun getArticlesByQuery(query: String): Flow<List<Article>> =
        articleDao.getArticlesByQuery(query)
            .map { entities -> entities.map { it.toDomain() } }

    fun getArticleByUrl(url: String): Flow<Article?> =
        articleDao.getArticleByUrl(url).map { it?.toDomain() }

    suspend fun insertArticles(articles: List<Article>) = withContext(Dispatchers.IO) {
        articleDao.insertArticles(articles.map { it.toEntity() })
    }

    suspend fun toggleFavorite(url: String) = withContext(Dispatchers.IO) {
        articleDao.toggleFavorite(url)
    }
}