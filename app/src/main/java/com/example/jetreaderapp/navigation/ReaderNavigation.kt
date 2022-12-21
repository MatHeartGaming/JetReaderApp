package com.example.jetreaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetreaderapp.screens.ReaderSplashScreen
import com.example.jetreaderapp.screens.details.BookDetailsScreen
import com.example.jetreaderapp.screens.home.Home
import com.example.jetreaderapp.screens.login.ReaderLoginScreen
import com.example.jetreaderapp.screens.search.ReaderSearchScreen
import com.example.jetreaderapp.screens.stats.ReaderStatsScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(route = ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }
        composable(route = ReaderScreens.ReaderHomeScreen.name) {
            Home(navController = navController)
        }
        composable(route = ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }
        composable(route = ReaderScreens.SearchScreen.name) {
            ReaderSearchScreen(navController = navController)
        }
        composable(route = ReaderScreens.DetailScreen.name) {
            BookDetailsScreen(navController = navController)
        }
        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
    }

}