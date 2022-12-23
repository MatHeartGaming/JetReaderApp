package com.example.jetreaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetreaderapp.screens.ReaderSplashScreen
import com.example.jetreaderapp.screens.details.BookDetailsScreen
import com.example.jetreaderapp.screens.home.Home
import com.example.jetreaderapp.screens.login.ReaderLoginScreen
import com.example.jetreaderapp.screens.search.BookSearchViewModel
import com.example.jetreaderapp.screens.search.BooksSearchViewModel
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
            val viewModel = hiltViewModel<BooksSearchViewModel>()
            ReaderSearchScreen(navController = navController, viewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })) {backStackEntry->
            backStackEntry.arguments?.getString("bookId").let {id->
                id?.let { BookDetailsScreen(navController = navController, bookId = it) }
            }
        }
        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
    }

}