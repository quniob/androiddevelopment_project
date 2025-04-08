package com.example.androiddevelopment_project.domain.usecase

import com.example.androiddevelopment_project.domain.model.MovieDomain
import com.example.androiddevelopment_project.domain.repository.MovieRepository

class GetMovieByIdUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(id: String): Result<MovieDomain> {
        return try {
            val movie = movieRepository.getMovieById(id)
            Result.success(movie)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 