package com.example.newsapp.feature.news.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.feature.news.domain.usecase.GetArticleUseCase
import com.example.newsapp.feature.news.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleUrl: String = checkNotNull(savedStateHandle["articleUrl"])

    val uiState: StateFlow<NewsDetailsUiState> = getArticleUseCase(articleUrl)
        .map { article ->
            if (article != null) NewsDetailsUiState.Success(article)
            else NewsDetailsUiState.Error("Articolo non trovato nel database locale.")
        }
        .catch {
            emit(NewsDetailsUiState.Error("Si è verificato un errore imprevisto."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsDetailsUiState.Loading
        )

    fun onEvent(event: NewsDetailsEvent) {
        when (event) {
            is NewsDetailsEvent.OnFavoriteClick -> {
                viewModelScope.launch {
                    toggleFavoriteUseCase(event.article)
                }
            }
        }
    }
}