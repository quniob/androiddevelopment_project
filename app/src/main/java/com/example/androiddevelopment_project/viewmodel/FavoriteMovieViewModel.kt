package com.example.androiddevelopment_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment_project.domain.usecase.AddMovieToFavoritesUseCase
import com.example.androiddevelopment_project.domain.usecase.GetFavoriteMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetFavoriteMoviesUseCase
import com.example.androiddevelopment_project.domain.usecase.IsMovieFavoriteUseCase
import com.example.androiddevelopment_project.domain.usecase.RemoveMovieFromFavoritesUseCase
import com.example.androiddevelopment_project.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteMovieViewModel(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val removeMovieFromFavoritesUseCase: RemoveMovieFromFavoritesUseCase,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase,
    private val getFavoriteMovieByIdUseCase: GetFavoriteMovieByIdUseCase
) : ViewModel() {
    
    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        getFavoriteMovies()
    }
    
    fun getFavoriteMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _favoriteMovies.value = getFavoriteMoviesUseCase()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addMovieToFavorites(movie: Movie) {
        viewModelScope.launch {
            try {
                addMovieToFavoritesUseCase(movie)
                getFavoriteMovies()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun removeMovieFromFavorites(movieId: String) {
        viewModelScope.launch {
            try {
                removeMovieFromFavoritesUseCase(movieId)
                getFavoriteMovies()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun isMovieFavorite(movieId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isFavorite = isMovieFavoriteUseCase(movieId)
                callback(isFavorite)
            } catch (e: Exception) {
                _error.value = e.message
                callback(false)
            }
        }
    }
    
    fun getMovieById(movieId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val movie = getFavoriteMovieByIdUseCase(movieId)
                _selectedMovie.value = movie
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при загрузке фильма из избранного"
                _selectedMovie.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
    }
} 