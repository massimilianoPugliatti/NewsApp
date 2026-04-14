package com.example.newsapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.feature.news.presentation.details.NewsDetailsScreen
import com.example.newsapp.feature.news.presentation.list.NewsListScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsList

@Serializable
class NewsDetails(val articleUrl: String)

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NewsList
    ) {
        composable<NewsList> {
            NewsListScreen { articleUrl ->
                navController.navigate(
                    NewsDetails(
                        articleUrl
                    )
                )
            }
        }
        composable<NewsDetails> { _ ->
            NewsDetailsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}