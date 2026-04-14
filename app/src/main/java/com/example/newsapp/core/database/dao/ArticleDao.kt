package com.example.newsapp.core.database.dao

import androidx.room.*
import com.example.newsapp.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("""
        SELECT * FROM articles 
        WHERE title LIKE '%' || :searchQuery || '%' 
        OR description LIKE '%' || :searchQuery || '%'
        ORDER BY publishedAt DESC
    """)
    fun getArticlesByQuery(searchQuery: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE url = :url")
    fun getArticleByUrl(url: String): Flow<ArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("UPDATE articles SET isFavorite = NOT isFavorite WHERE url = :url")
    suspend fun toggleFavorite(url: String)
}