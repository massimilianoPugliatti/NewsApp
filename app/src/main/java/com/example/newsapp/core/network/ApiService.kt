package com.example.newsapp.core.network

import com.example.newsapp.feature.news.data.repository.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "it",
        @Query("pageSize") pageSize: Int = 100
    ): NewsResponseDto
}