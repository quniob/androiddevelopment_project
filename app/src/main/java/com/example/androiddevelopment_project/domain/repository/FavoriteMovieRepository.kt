package com.example.androiddevelopment_project.domain.repository

import com.example.androiddevelopment_project.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoriteMovieRepository {
    fun getAllFavoriteMovies(): Flow<List<Movie>>
    suspend fun addMovieToFavorites(movie: Movie)
    suspend fun removeMovieFromFavorites(movieId: String)
    fun isMovieFavorite(movieId: String): Flow<Boolean>
    suspend fun getFavoriteMovieById(movieId: String): Movie?
} 