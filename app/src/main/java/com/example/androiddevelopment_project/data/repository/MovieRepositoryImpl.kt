package com.example.androiddevelopment_project.data.repository

import com.example.androiddevelopment_project.data.network.MovieDataSource
import com.example.androiddevelopment_project.domain.model.MovieDomain
import com.example.androiddevelopment_project.domain.repository.MovieRepository
import com.example.androiddevelopment_project.GetMovieByIdQuery
import com.example.androiddevelopment_project.GetPopularMoviesQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val movieDataSource: MovieDataSource
) : MovieRepository {

    override suspend fun getMovieById(id: String): MovieDomain = withContext(Dispatchers.IO) {
        val response = movieDataSource.getMovieById(id)
        mapToDomainModel(response)
    }

    override suspend fun getPopularMovies(): List<MovieDomain> = withContext(Dispatchers.IO) {
        val movies = movieDataSource.getPopularMovies()
        movies.map { mapToMovieDomain(it) }
    }

    private fun mapToDomainModel(data: GetMovieByIdQuery.Data?): MovieDomain {
        val title = data?.title
        
        val director = title?.credits?.find { it.category == "director" }?.name?.display_name ?: ""
        
        val actors = title?.credits
            ?.filter { it.category == "actor" || it.category == "actress" }
            ?.mapNotNull { it.name?.display_name }
            ?: emptyList()
            
        val language = title?.spoken_languages?.firstOrNull()?.name ?: ""
        val country = title?.origin_countries?.firstOrNull()?.name ?: ""
        
        return MovieDomain(
            id = title?.id ?: "",
            title = title?.primary_title ?: "",
            year = title?.start_year ?: 0,
            description = title?.plot ?: "",
            posterUrl = title?.posters?.firstOrNull()?.url ?: "",
            rating = title?.rating?.aggregate_rating?.toDouble() ?: 0.0,
            genres = title?.genres?.filterNotNull() ?: emptyList(),
            director = director,
            actors = actors,
            runtime = "${title?.runtime_minutes ?: 0} мин",
            language = language,
            country = country,
            awards = null,
            boxOffice = null,
            production = null,
            releaseDate = null
        )
    }

    private fun mapToMovieDomain(title: GetPopularMoviesQuery.Title): MovieDomain {
        return MovieDomain(
            id = title.id,
            title = title.primary_title,
            year = title.start_year ?: 0,
            description = "",
            posterUrl = title.posters?.firstOrNull()?.url ?: "",
            rating = title.rating?.aggregate_rating?.toDouble() ?: 0.0,
            genres = title.genres?.filterNotNull() ?: emptyList(),
            director = "",
            actors = emptyList(),
            runtime = "",
            language = "",
            country = "",
            awards = null,
            boxOffice = null,
            production = null,
            releaseDate = null
        )
    }
} 