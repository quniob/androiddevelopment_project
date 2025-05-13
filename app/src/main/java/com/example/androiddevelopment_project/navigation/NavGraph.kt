package com.example.androiddevelopment_project.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androiddevelopment_project.screen.EditProfileScreen
import com.example.androiddevelopment_project.screen.FavoritesScreen
import com.example.androiddevelopment_project.screen.FilterScreen
import com.example.androiddevelopment_project.screen.HomeScreen
import com.example.androiddevelopment_project.screen.MovieDetailScreen
import com.example.androiddevelopment_project.screen.ProfileScreen
import com.example.androiddevelopment_project.ui.badge.BadgeStateHolder
import com.example.androiddevelopment_project.viewmodel.MovieViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Favorites : Screen("favorites")
    object Filter : Screen("filter")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val badgeRequired: Boolean = false
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        label = "Фильмы",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = Screen.Search.route,
        label = "Фильтры",
        icon = Icons.Default.Settings,
        badgeRequired = true
    ),
    BottomNavItem(
        route = Screen.Favorites.route,
        label = "Избранное",
        icon = Icons.Default.Favorite
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        label = "Профиль",
        icon = Icons.Default.Person
    )
)

@Composable
fun BadgeNavIcon(
    icon: ImageVector,
    badgeStateHolder: BadgeStateHolder,
    contentDescription: String? = null
) {
    val showBadge by badgeStateHolder.showBadge.collectAsState()
    
    if (showBadge) {
        BadgedBox(
            badge = {
                Badge()
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    } else {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    badgeStateHolder: BadgeStateHolder = get(),
    movieViewModel: MovieViewModel = koinViewModel()
) {
    val hasActiveFilters by movieViewModel.hasActiveFilters.collectAsState(initial = false)
    badgeStateHolder.updateBadgeState(hasActiveFilters)

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
            FilterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onApplyFilters = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onEditClick = {
                    navController.navigate(Screen.EditProfile.route)
                }
            )
        }
        
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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