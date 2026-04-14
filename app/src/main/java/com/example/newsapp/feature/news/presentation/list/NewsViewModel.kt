package com.example.newsapp.feature.news.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.feature.news.domain.model.Article
import com.example.newsapp.feature.news.domain.model.NewsError
import com.example.newsapp.feature.news.domain.usecase.GetNewsUseCase
import com.example.newsapp.feature.news.domain.usecase.SearchNewsUseCase
import com.example.newsapp.feature.news.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("android")
    val searchQuery = _searchQuery.asStateFlow()
    private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Idle)
    private val _uiEffect = Channel<NewsListEffect>()
    val uiEvent = _uiEffect.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<NewsListUiState> = combine(
        _searchQuery.flatMapLatest { query -> getNewsUseCase(query) },
        _networkStatus,
        ::mapToUiState
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsListUiState.Loading
        )

    init {
        performSearch(_searchQuery.value)
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _networkStatus.update { NetworkStatus.Loading }
            searchNewsUseCase(query)
                .onSuccess {
                    _uiEffect.send(NewsListEffect.ScrollToTop)
                    _networkStatus.update { NetworkStatus.Success }
                }
                .onFailure { error ->
                    if (error is NewsError.NoResults) {
                        _networkStatus.update { NetworkStatus.Success }
                        _uiEffect.send(NewsListEffect.ShowSnackbar("Nessun risultato trovato."))
                    } else {
                        val message = when (error) {
                            is NewsError.NetworkError -> "Sei Offline! Controlla la tua connessione."
                            else -> "Errore di comunicazione con il server."
                        }
                        _networkStatus.update { NetworkStatus.Error(message) }
                        _uiEffect.send(NewsListEffect.ShowSnackbar(message))
                    }
                }
        }
    }

    private fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            toggleFavoriteUseCase(article)
        }
    }

    fun onEvent(event: NewsListEvent) {
        when (event) {
            is NewsListEvent.OnSearchQueryChanged -> _searchQuery.update { event.query }
            is NewsListEvent.OnSearchTriggered -> performSearch(_searchQuery.value)
            is NewsListEvent.OnFavoriteClick -> toggleFavorite(event.article)
            is NewsListEvent.OnRefresh -> performSearch(_searchQuery.value)
        }
    }

    private fun mapToUiState(articles: List<Article>, status: NetworkStatus): NewsListUiState {
        return when (status) {
            is NetworkStatus.Loading -> if (articles.isEmpty()) NewsListUiState.Loading else NewsListUiState.Success(
                articles
            )

            is NetworkStatus.Error -> if (articles.isEmpty()) NewsListUiState.Error(status.message) else NewsListUiState.Success(
                articles
            )

            else -> NewsListUiState.Success(articles)
        }
    }
}
