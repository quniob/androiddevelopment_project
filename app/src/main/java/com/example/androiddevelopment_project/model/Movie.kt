package com.example.androiddevelopment_project.model


data class Movie(
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
    val awards: String,
    val boxOffice: String,
    val production: String,
    val releaseDate: String
)


fun getMockMovies(): List<Movie> {
    return listOf(
        Movie(
            id = "tt0111161",
            title = "Побег из Шоушенка",
            year = 1994,
            description = "Две пожизненные судимости за один побег из Шоушенка",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_FMjpg_UX674_.jpg",
            rating = 9.3,
            genres = listOf("Драма"),
            director = "Фрэнк Дарабонт",
            actors = listOf("Тим Роббинс", "Морган Фриман", "Боб Гантон"),
            runtime = "142 мин",
            language = "Английский",
            country = "США",
            awards = "Номинации на 7 премий Оскар",
            boxOffice = "$28,767,189",
            production = "Castle Rock Entertainment",
            releaseDate = "14 октября 1994"
        ),
        Movie(
            id = "tt0068646",
            title = "Крестный отец",
            year = 1972,
            description = "История семьи Корлеоне — могущественного клана итало-американской мафии",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX674_.jpg",
            rating = 9.2,
            genres = listOf("Криминал", "Драма"),
            director = "Фрэнсис Форд Коппола",
            actors = listOf("Марлон Брандо", "Аль Пачино", "Джеймс Каан"),
            runtime = "175 мин",
            language = "Английский, Итальянский, Латинский",
            country = "США",
            awards = "Оскар, Золотой глобус, BAFTA",
            boxOffice = "$134,966,411",
            production = "Paramount Pictures",
            releaseDate = "24 марта 1972"
        ),
        Movie(
            id = "tt0468569",
            title = "Темный рыцарь",
            year = 2008,
            description = "Когда Джокер появляется в Готэме, Бэтмен вынужден противостоять ему",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_FMjpg_UX674_.jpg",
            rating = 9.0,
            genres = listOf("Боевик", "Криминал", "Драма"),
            director = "Кристофер Нолан",
            actors = listOf("Кристиан Бэйл", "Хит Леджер", "Аарон Экхарт"),
            runtime = "152 мин",
            language = "Английский, Мандаринский",
            country = "США, Великобритания",
            awards = "Оскар, Золотой глобус, BAFTA",
            boxOffice = "$1,005,973,645",
            production = "Warner Bros.",
            releaseDate = "18 июля 2008"
        ),
    )
} 