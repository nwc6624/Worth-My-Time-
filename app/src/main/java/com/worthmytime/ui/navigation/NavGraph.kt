package com.worthmytime.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.worthmytime.ui.goals.GoalsScreen
import com.worthmytime.ui.home.HomeScreen
import com.worthmytime.ui.insights.InsightsScreen
import com.worthmytime.ui.settings.SettingsScreen

sealed class Screen(val route: String, val titleResId: Int, val iconResId: Int) {
    object Home : Screen("home", com.worthmytime.R.string.nav_home, com.worthmytime.R.drawable.ic_home)
    object Goals : Screen("goals", com.worthmytime.R.string.nav_goals, com.worthmytime.R.drawable.ic_goals)
    object Insights : Screen("insights", com.worthmytime.R.string.nav_insights, com.worthmytime.R.drawable.ic_insights)
    object Settings : Screen("settings", com.worthmytime.R.string.nav_settings, com.worthmytime.R.drawable.ic_settings)
}

val screens = listOf(
    Screen.Home,
    Screen.Goals,
    Screen.Insights,
    Screen.Settings
)

@Composable
fun WorthMyTimeNavGraph(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(id = screen.iconResId), contentDescription = null) },
                        label = { Text(stringResource(screen.titleResId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Goals.route) {
                GoalsScreen()
            }
            composable(Screen.Insights.route) {
                InsightsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
