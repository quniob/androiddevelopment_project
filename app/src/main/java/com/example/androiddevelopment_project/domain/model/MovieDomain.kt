package com.example.androiddevelopment_project.domain.model

data class MovieDomain(
    val id: String,
    val title: String,
    val year: Int,
    val description: String,
    val posterUrl: String,
    val rating: Double,
    val genres: List<String>,
    val director: String,
    val actors: List<String>,
    val runtime: String,
    val language: String,
    val country: String,
    val awards: String?,
    val boxOffice: String?,
    val production: String?,
    val releaseDate: String?
) 