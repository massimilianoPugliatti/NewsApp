package com.example.newsapp.feature.news.domain.usecase

import android.util.Log
import com.example.newsapp.feature.news.domain.model.NewsError
import com.example.newsapp.feature.news.domain.repository.NewsRepository
import javax.inject.Inject
class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String): Result<Unit> {
        return try {
            repository.searchNews(query)
            Result.success(Unit)
        } catch (e: NewsError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(NewsError.UnknownError())
        }
    }
}