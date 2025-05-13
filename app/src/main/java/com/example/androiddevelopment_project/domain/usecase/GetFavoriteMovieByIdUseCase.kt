package com.example.androiddevelopment_project.domain.usecase

import com.example.androiddevelopment_project.domain.repository.FavoriteMovieRepository
import com.example.androiddevelopment_project.model.Movie

class GetFavoriteMovieByIdUseCase(
    private val repository: FavoriteMovieRepository
) {
    suspend operator fun invoke(movieId: String): Movie {
        return repository.getFavoriteMovieById(movieId) 
            ?: throw Exception("Фильм не найден в избранном")
    }
} 