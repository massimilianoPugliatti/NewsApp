package com.example.newsapp.feature.news.domain.model

import com.example.newsapp.core.database.entity.ArticleEntity
import java.time.LocalDateTime


data class Article(
    val url: String,
    val title: String,
    val author: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: LocalDateTime,
    val sourceName: String,
    val isFavorite: Boolean = false
) {
    fun toEntity(): ArticleEntity {
        return ArticleEntity(url, title, author, description, urlToImage, publishedAt, sourceName, isFavorite)
    }
}
