package com.example.androiddevelopment_project.data.repository

import com.example.androiddevelopment_project.data.db.dao.FavoriteMovieDao
import com.example.androiddevelopment_project.data.db.entity.FavoriteMovieEntity
import com.example.androiddevelopment_project.domain.repository.FavoriteMovieRepository
import com.example.androiddevelopment_project.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteMovieRepositoryImpl(
    private val favoriteMovieDao: FavoriteMovieDao
) : FavoriteMovieRepository {
    
    override fun getAllFavoriteMovies(): Flow<List<Movie>> {
        return favoriteMovieDao.getAllFavoriteMovies().map { entities ->
            entities.map { it.toMovie() }
        }
    }
    
    override suspend fun addMovieToFavorites(movie: Movie) {
        favoriteMovieDao.insertFavoriteMovie(movie.toEntity())
    }
    
    override suspend fun removeMovieFromFavorites(movieId: String) {
        favoriteMovieDao.deleteFavoriteMovieById(movieId)
    }
    
    override fun isMovieFavorite(movieId: String): Flow<Boolean> {
        return favoriteMovieDao.isMovieFavorite(movieId)
    }
    
    override suspend fun getFavoriteMovieById(movieId: String): Movie? {
        val entity = favoriteMovieDao.getFavoriteMovieById(movieId)
        return entity?.toMovie()
    }
    
    private fun FavoriteMovieEntity.toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            year = year,
            description = description,
            posterUrl = posterUrl,
            rating = rating,
            genres = genres,
            director = director,
            actors = actors,
            runtime = runtime,
            language = language,
            country = country,
            awards = awards,
            boxOffice = boxOffice,
            production = production,
            releaseDate = releaseDate
        )
    }
    
    private fun Movie.toEntity(): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = id,
            title = title,
            year = year,
            description = description,
            posterUrl = posterUrl,
            rating = rating,
            genres = genres,
            director = director,
            actors = actors,
            runtime = runtime,
            language = language,
            country = country,
            awards = awards,
            boxOffice = boxOffice,
            production = production,
            releaseDate = releaseDate
        )
    }
} 