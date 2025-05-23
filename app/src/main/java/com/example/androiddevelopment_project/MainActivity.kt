package com.example.androiddevelopment_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androiddevelopment_project.navigation.AppNavGraph
import com.example.androiddevelopment_project.navigation.BadgeNavIcon
import com.example.androiddevelopment_project.navigation.Screen
import com.example.androiddevelopment_project.navigation.bottomNavItems
import com.example.androiddevelopment_project.ui.badge.BadgeStateHolder
import com.example.androiddevelopment_project.ui.theme.AndroidDevelopment_ProjectTheme
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidDevelopment_ProjectTheme {
                MovieApp()
            }
        }
    }
}

@Composable
fun MovieApp(badgeStateHolder: BadgeStateHolder = get()) {
    val navController = rememberNavController()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val showBottomBar = when (currentDestination?.route) {
                    Screen.Home.route, Screen.Search.route, 
                    Screen.Profile.route, Screen.Favorites.route -> true
                    else -> false
                }
                
                if (showBottomBar) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                            
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    if (item.badgeRequired && item.route == Screen.Search.route) {
                                        BadgeNavIcon(
                                            icon = item.icon,
                                            badgeStateHolder = badgeStateHolder,
                                            contentDescription = item.label
                                        )
                                    } else {
                                        androidx.compose.material3.Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label
                                        )
                                    }
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            AppNavGraph(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}