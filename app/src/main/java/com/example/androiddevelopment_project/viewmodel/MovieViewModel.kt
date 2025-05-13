package com.example.androiddevelopment_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment_project.data.preferences.FilterPreferencesManager
import com.example.androiddevelopment_project.domain.model.MovieDomain
import com.example.androiddevelopment_project.domain.usecase.GetMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetPopularMoviesUseCase
import com.example.androiddevelopment_project.model.Movie
import com.example.androiddevelopment_project.model.MovieFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieViewModel(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val filterPreferencesManager: FilterPreferencesManager
) : ViewModel() {
    private val _allMovies = MutableStateFlow<List<Movie>>(emptyList())
    
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _filters = MutableStateFlow(MovieFilters())
    val filters: StateFlow<MovieFilters> = _filters.asStateFlow()
    
    val hasActiveFilters = filterPreferencesManager.hasActiveFilters

    init {
        loadPopularMovies()
        loadSavedFilters()
    }
    
    private fun loadSavedFilters() {
        viewModelScope.launch {
            val genre = filterPreferencesManager.selectedGenre.first()
            val rating = filterPreferencesManager.minRating.first()
            val year = filterPreferencesManager.releaseYear.first()
            
            _filters.value = MovieFilters(
                selectedGenre = genre,
                minRating = rating,
                releaseYear = year
            )
            
            applyFilters()
        }
    }
    
    fun updateGenreFilter(genre: String) {
        _filters.value = _filters.value.copy(selectedGenre = genre)
        viewModelScope.launch {
            filterPreferencesManager.saveSelectedGenre(genre)
            applyFilters()
        }
    }
    
    fun updateMinRatingFilter(rating: Double) {
        _filters.value = _filters.value.copy(minRating = rating)
        viewModelScope.launch {
            filterPreferencesManager.saveMinRating(rating)
            applyFilters()
        }
    }
    
    fun updateReleaseYearFilter(year: String) {
        _filters.value = _filters.value.copy(releaseYear = year)
        viewModelScope.launch {
            filterPreferencesManager.saveReleaseYear(year)
            applyFilters()
        }
    }
    
    fun resetFilters() {
        _filters.value = MovieFilters()
        viewModelScope.launch {
            filterPreferencesManager.resetFilters()
            applyFilters()
        }
    }

    private fun applyFilters() {
        val filteredMovies = _allMovies.value.filter { movie ->
            var matches = true
            
            // Проверка жанра
            if (_filters.value.selectedGenre.isNotEmpty()) {
                matches = matches && movie.genres.contains(_filters.value.selectedGenre)
            }
            
            // Проверка рейтинга
            if (_filters.value.minRating > 0) {
                matches = matches && movie.rating >= _filters.value.minRating
            }
            
            // Проверка года выпуска
            if (_filters.value.releaseYear.isNotEmpty()) {
                matches = matches && movie.year.toString() == _filters.value.releaseYear
            }
            
            matches
        }
        
        _movies.value = filteredMovies
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
                    val mappedMovies = movies.map { mapDomainToUiModel(it) }
                    _allMovies.value = mappedMovies
                    applyFilters()
                    _error.value = null
                }
                .onFailure { error ->
                    _error.value = "Ошибка загрузки популярных фильмов: ${error.message}"
                    _allMovies.value = emptyList()
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