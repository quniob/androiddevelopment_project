package com.example.androiddevelopment_project.model

data class MovieFilters(
    val selectedGenre: String = "",
    val minRating: Double = 0.0,
    val releaseYear: String = ""
) {
    fun hasActiveFilters(): Boolean {
        return selectedGenre.isNotEmpty() || minRating > 0.0 || releaseYear.isNotEmpty()
    }
}

val genreMap = mapOf(
    "" to "Все жанры",
    "Drama" to "Драма",
    "Crime" to "Криминал",
    "Action" to "Боевик",
    "Comedy" to "Комедия",
    "Sci-Fi" to "Научная фантастика",
    "Thriller" to "Триллер", 
    "Horror" to "Ужасы",
    "Adventure" to "Приключения",
    "Romance" to "Мелодрама",
    "Fantasy" to "Фэнтези",
    "Animation" to "Мультфильм",
    "Documentary" to "Документальный",
    "Biography" to "Биография",
    "History" to "Исторический",
    "Mystery" to "Детектив",
    "War" to "Военный",
    "Western" to "Вестерн",
    "Music" to "Музыкальный",
    "Sport" to "Спортивный"
)

val availableGenres = genreMap.keys.toList()