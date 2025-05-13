package com.example.androiddevelopment_project.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androiddevelopment_project.data.db.converter.StringListConverter

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val year: Int,
    val description: String,
    val posterUrl: String,
    val rating: Double,
    @TypeConverters(StringListConverter::class)
    val genres: List<String>,
    val director: String,
    @TypeConverters(StringListConverter::class)
    val actors: List<String>,
    val runtime: String,
    val language: String,
    val country: String,
    val awards: String,
    val boxOffice: String,
    val production: String,
    val releaseDate: String
) 