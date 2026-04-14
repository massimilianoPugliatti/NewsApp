package com.example.newsapp.feature.news.presentation.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.feature.news.domain.model.Article

@Composable
fun ArticleDetailContent(article: Article, onFavoriteToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .background(Color.LightGray),
            contentScale = ContentScale.Fit,
            onLoading = { Log.d("Coil:", "Caricamento in corso...") },
            onError = { state ->
                Log.d("Coil:", "error ${state.result.throwable.message}")
            },
            onSuccess = {
                Log.d("Coil:", "Immagine caricata con successo!")
            },
            error = painterResource(R.drawable.offline_svgrepo_com),
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.sourceName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (article.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Preferiti",
                        tint = if (article.isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Text(text = article.title, style = MaterialTheme.typography.headlineSmall)

            Text(
                text = "Pubblicato il: ${article.publishedAt}", // Qui puoi aggiungere un formatter
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = article.description ?: "Nessun contenuto disponibile.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}