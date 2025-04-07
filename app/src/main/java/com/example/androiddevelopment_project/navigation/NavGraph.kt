package com.example.androiddevelopment_project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androiddevelopment_project.screen.HomeScreen
import com.example.androiddevelopment_project.screen.MovieDetailScreen
import com.example.androiddevelopment_project.screen.ProfileScreen
import com.example.androiddevelopment_project.screen.SearchScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
}

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        label = "Фильмы",
        icon = "home"
    ),
    BottomNavItem(
        route = Screen.Search.route,
        label = "Поиск",
        icon = "search"
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        label = "Профиль",
        icon = "person"
    )
)

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String
)

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen()
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        
        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
} 