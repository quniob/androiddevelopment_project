package com.example.androiddevelopment_project.domain.usecase

import com.example.androiddevelopment_project.domain.repository.FavoriteMovieRepository
import com.example.androiddevelopment_project.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetFavoriteMoviesUseCase(private val favoriteMovieRepository: FavoriteMovieRepository) {
    suspend operator fun invoke(): List<Movie> {
        return favoriteMovieRepository.getAllFavoriteMovies().first()
    }
}

class AddMovieToFavoritesUseCase(private val favoriteMovieRepository: FavoriteMovieRepository) {
    suspend operator fun invoke(movie: Movie) {
        favoriteMovieRepository.addMovieToFavorites(movie)
    }
}

class RemoveMovieFromFavoritesUseCase(private val favoriteMovieRepository: FavoriteMovieRepository) {
    suspend operator fun invoke(movieId: String) {
        favoriteMovieRepository.removeMovieFromFavorites(movieId)
    }
}

class IsMovieFavoriteUseCase(private val favoriteMovieRepository: FavoriteMovieRepository) {
    suspend operator fun invoke(movieId: String): Boolean {
        return favoriteMovieRepository.isMovieFavorite(movieId).first()
    }
}