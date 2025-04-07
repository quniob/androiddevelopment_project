package com.example.androiddevelopment_project.viewmodel

import androidx.lifecycle.ViewModel
import com.example.androiddevelopment_project.model.Movie
import com.example.androiddevelopment_project.model.getMockMovies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        _isLoading.value = true
        try {
            _movies.value = getMockMovies()
            _error.value = null
        } catch (e: Exception) {
            _error.value = "ошибка: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun getMovieById(movieId: String) {
        _isLoading.value = true
        try {
            val movie = _movies.value.find { it.id == movieId }
            _selectedMovie.value = movie
            _error.value = if (movie == null) "фильм не найден" else null
        } catch (e: Exception) {
            _error.value = "ошибка: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
} 