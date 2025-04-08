package com.example.androiddevelopment_project.domain.usecase

import com.example.androiddevelopment_project.domain.model.MovieDomain
import com.example.androiddevelopment_project.domain.repository.MovieRepository

class GetPopularMoviesUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(): Result<List<MovieDomain>> {
        return try {
            val movies = movieRepository.getPopularMovies()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 