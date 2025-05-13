package com.example.androiddevelopment_project.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevelopment_project.viewmodel.FavoriteMovieViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onMovieClick: (String) -> Unit,
    viewModel: FavoriteMovieViewModel = koinViewModel()
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            favoriteMovies.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "У вас пока нет избранных фильмов",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                MovieList(
                    movies = favoriteMovies,
                    onMovieClick = onMovieClick,
                    contentPadding = PaddingValues(16.dp)
                )
            }
        }
    }
} 