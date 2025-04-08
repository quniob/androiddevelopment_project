package com.example.androiddevelopment_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment_project.domain.model.MovieDomain
import com.example.androiddevelopment_project.domain.usecase.GetMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetPopularMoviesUseCase
import com.example.androiddevelopment_project.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadPopularMovies()
    }

    fun getMovieById(movieId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            getMovieByIdUseCase(movieId)
                .onSuccess { movie ->
                    _selectedMovie.value = mapDomainToUiModel(movie)
                    _error.value = null
                }
                .onFailure { error ->
                    _error.value = "Ошибка: ${error.message}"
                }
            _isLoading.value = false
        }
    }
    
    private fun loadPopularMovies() {
        _isLoading.value = true
        viewModelScope.launch {
            getPopularMoviesUseCase()
                .onSuccess { movies ->
                    _movies.value = movies.map { mapDomainToUiModel(it) }
                    _error.value = null
                }
                .onFailure { error ->
                    _error.value = "Ошибка загрузки популярных фильмов: ${error.message}"
                    _movies.value = emptyList()
                }
            _isLoading.value = false
        }
    }

    private fun mapDomainToUiModel(movie: MovieDomain): Movie {
        return Movie(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            description = movie.description,
            posterUrl = movie.posterUrl,
            rating = movie.rating,
            genres = movie.genres,
            director = movie.director,
            actors = movie.actors,
            runtime = movie.runtime,
            language = movie.language,
            country = movie.country,
            awards = movie.awards ?: "",
            boxOffice = movie.boxOffice ?: "",
            production = movie.production ?: "",
            releaseDate = movie.releaseDate ?: ""
        )
    }
} 