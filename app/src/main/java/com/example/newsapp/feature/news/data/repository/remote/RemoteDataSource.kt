package com.example.newsapp.feature.news.data.repository.remote

import com.example.newsapp.BuildConfig
import com.example.newsapp.core.network.ApiService
import com.example.newsapp.feature.news.domain.model.Article
import java.time.LocalDate
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchNews(query: String): List<Article> {
        val fromDate = LocalDate.now().minusDays(30).toString()

        val response = apiService.getNews(
            query = query,
            fromDate = fromDate,
            apiKey = BuildConfig.NEWS_API_KEY
        )
        return response.articles.map { it.toDomain() }
    }
}