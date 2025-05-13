package com.example.androiddevelopment_project.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androiddevelopment_project.data.db.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>>
    
    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteMovieById(movieId: String): FavoriteMovieEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity)
    
    @Delete
    suspend fun deleteFavoriteMovie(movie: FavoriteMovieEntity)
    
    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteFavoriteMovieById(movieId: String)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId LIMIT 1)")
    fun isMovieFavorite(movieId: String): Flow<Boolean>
} 