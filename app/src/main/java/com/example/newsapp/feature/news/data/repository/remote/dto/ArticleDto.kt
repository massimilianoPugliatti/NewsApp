package com.example.newsapp.feature.news.data.repository.remote.dto

import com.example.newsapp.feature.news.domain.model.Article
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ArticleDto(
    val source: SourceDto,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    val publishedAt: LocalDateTime,
    val content: String?
) {

    fun toDomain(): Article {
        return Article(url, title.orEmpty(), author, description, urlToImage, publishedAt, source.name)
    }

}
