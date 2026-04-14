package com.example.newsapp.feature.news.data.repository

import android.util.Log
import com.example.newsapp.feature.news.data.repository.local.LocalDataSource
import com.example.newsapp.feature.news.data.repository.remote.RemoteDataSource
import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.model.NewsError
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : NewsRepository {

    override fun getArticlesByQuery(query: String): Flow<List<Article>> =
        localDataSource.getArticlesByQuery(query)

    override suspend fun searchNews(query: String) {
        try {
            val response = remoteDataSource.fetchNews(query)

            if (response.isEmpty()) {
                throw NewsError.NoResults()
            }

            localDataSource.insertArticles(response)

        } catch (e: java.io.IOException) {
            Log.e("Error","$e.message")
            throw NewsError.NetworkError()
        } catch (e: retrofit2.HttpException) {
            Log.e("Error","$e.message")
            throw NewsError.ServerError()
        } catch (e: NewsError) {
            throw e
        } catch (e: Exception) {
            Log.e("Error","$e.message")
            throw NewsError.UnknownError()
        }
    }

    override fun getArticleByUrl(url: String): Flow<Article?> {
        return localDataSource.getArticleByUrl(url)
    }

    override suspend fun toggleFavorite(article: Article) {
        localDataSource.toggleFavorite(article.url)
    }

}