package com.example.androiddevelopment_project.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")

class FilterPreferencesManager(private val context: Context) {
    
    companion object {
        val SELECTED_GENRE = stringPreferencesKey("selected_genre")
        val MIN_RATING = doublePreferencesKey("min_rating")
        val RELEASE_YEAR = stringPreferencesKey("release_year")
    }
    
    val selectedGenre: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_GENRE] ?: ""
        }
    
    val minRating: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[MIN_RATING] ?: 0.0
        }
    
    val releaseYear: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[RELEASE_YEAR] ?: ""
        }
    
    suspend fun saveSelectedGenre(genre: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_GENRE] = genre
        }
    }
    
    suspend fun saveMinRating(rating: Double) {
        context.dataStore.edit { preferences ->
            preferences[MIN_RATING] = rating
        }
    }
    
    suspend fun saveReleaseYear(year: String) {
        context.dataStore.edit { preferences ->
            preferences[RELEASE_YEAR] = year
        }
    }
    
    suspend fun resetFilters() {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_GENRE)
            preferences.remove(MIN_RATING)
            preferences.remove(RELEASE_YEAR)
        }
    }
    
    val hasActiveFilters: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            val hasGenre = preferences[SELECTED_GENRE]?.isNotEmpty() ?: false
            val hasMinRating = preferences[MIN_RATING] != null && preferences[MIN_RATING]!! > 0.0
            val hasYear = preferences[RELEASE_YEAR]?.isNotEmpty() ?: false
            
            hasGenre || hasMinRating || hasYear
        }
} 