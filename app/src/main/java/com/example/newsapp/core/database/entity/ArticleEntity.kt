package com.example.newsapp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.feature.news.domain.model.Article
import java.time.LocalDateTime

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val title: String,
    val author: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: LocalDateTime,
    val sourceName: String,
    val isFavorite: Boolean = false
){
    fun toDomain() = Article(url, title, author, description, urlToImage, publishedAt, sourceName, isFavorite)
}

