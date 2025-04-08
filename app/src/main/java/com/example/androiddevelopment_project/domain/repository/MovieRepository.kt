package com.example.androiddevelopment_project.domain.repository

import com.example.androiddevelopment_project.domain.model.MovieDomain

interface MovieRepository {
    suspend fun getMovieById(id: String): MovieDomain
    suspend fun getPopularMovies(): List<MovieDomain>
} 