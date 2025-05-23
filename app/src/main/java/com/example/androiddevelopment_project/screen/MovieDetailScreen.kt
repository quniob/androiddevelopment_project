package com.example.androiddevelopment_project.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.androiddevelopment_project.model.genreMap
import com.example.androiddevelopment_project.viewmodel.FavoriteMovieViewModel
import com.example.androiddevelopment_project.viewmodel.MovieViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: String,
    onBackClick: () -> Unit,
    viewModel: MovieViewModel = koinViewModel(),
    favoriteViewModel: FavoriteMovieViewModel = koinViewModel()
) {
    val selectedMovie by viewModel.selectedMovie.collectAsState()
    val favoriteMovie by favoriteViewModel.selectedMovie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavoriteLoading by favoriteViewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val favoriteError by favoriteViewModel.error.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
    
    val movieToShow = selectedMovie ?: favoriteMovie
    val showError = error != null && favoriteError != null
    val loading = isLoading || isFavoriteLoading

    LaunchedEffect(movieId) {
        viewModel.getMovieById(movieId)
    }
    
    LaunchedEffect(error) {
        if (error != null) {
            favoriteViewModel.getMovieById(movieId)
        }
    }
    
    LaunchedEffect(movieToShow) {
        if (movieToShow != null) {
            favoriteViewModel.isMovieFavorite(movieId) { isFav ->
                isFavorite = isFav
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movieToShow?.title ?: "Детали фильма") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (movieToShow != null) {
                        IconButton(
                            onClick = {
                                if (isFavorite) {
                                    favoriteViewModel.removeMovieFromFavorites(movieId)
                                } else {
                                    movieToShow.let { movie ->
                                        favoriteViewModel.addMovieToFavorites(movie)
                                    }
                                }
                                isFavorite = !isFavorite
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                showError -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = favoriteError ?: error ?: "Ошибка загрузки фильма",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                movieToShow != null -> {
                    val movie = movieToShow
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            val (poster, title, year, rating, genres) = createRefs()
                            
                            AsyncImage(
                                model = movie.posterUrl,
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .constrainAs(poster) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        width = Dimension.value(120.dp)
                                        height = Dimension.value(180.dp)
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.constrainAs(title) {
                                    top.linkTo(parent.top)
                                    start.linkTo(poster.end, margin = 16.dp)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                            )
                            
                            Text(
                                text = "Год: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.constrainAs(year) {
                                    top.linkTo(title.bottom, margin = 8.dp)
                                    start.linkTo(title.start)
                                    end.linkTo(title.end, margin = 4.dp)
                                    width = Dimension.fillToConstraints
                                }
                            )
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.constrainAs(rating) {
                                    top.linkTo(year.bottom, margin = 8.dp)
                                    start.linkTo(title.start)
                                }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(getRatingColor(movie.rating))
                                ) {
                                    Text(
                                        text = movie.rating.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "IMDb рейтинг",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            Text(
                                text = movie.genres.joinToString(", ") { genre -> 
                                    genreMap[genre] ?: genre 
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.constrainAs(genres) {
                                    top.linkTo(rating.bottom, margin = 8.dp)
                                    start.linkTo(title.start)
                                    end.linkTo(title.end)
                                    width = Dimension.fillToConstraints
                                }
                            )
                        }
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Описание",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = movie.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                
                                InfoRow(label = "Режиссер", value = movie.director)
                                InfoRow(label = "В ролях", value = movie.actors.joinToString(", "))
                                InfoRow(label = "Длительность", value = movie.runtime)
                                InfoRow(label = "Язык", value = movie.language)
                                InfoRow(label = "Страна", value = movie.country)
                                InfoRow(label = "Награды", value = movie.awards)
                                InfoRow(label = "Кассовые сборы", value = movie.boxOffice)
                                InfoRow(label = "Производство", value = movie.production)
                                InfoRow(label = "Дата выхода", value = movie.releaseDate)
                            }
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Фильм не найден",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun getRatingColor(rating: Double): Color {
    return when {
        rating >= 8.0 -> Color(0xFF388E3C)
        rating >= 6.0 -> Color(0xFFFFA000)
        else -> Color(0xFFD32F2F)
    }
} 