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
            Log.e("Error","$e.message")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("Error","$e.message")
            Result.failure(NewsError.UnknownError())
        }
    }
}