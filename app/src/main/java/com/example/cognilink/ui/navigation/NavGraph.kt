package com.example.cognilink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cognilink.ui.screens.auth.AuthScreen
import com.example.cognilink.ui.screens.auth.TermsScreen
import com.example.cognilink.ui.screens.deck.DeckScreen
import com.example.cognilink.ui.screens.home.ProfileScreen
import com.example.cognilink.ui.screens.home.HomeScreen
import com.example.cognilink.ui.viewmodels.TermsViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Terms : Screen("terms")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Long) = "profile/$userId"
    }
    object Home: Screen("home/{userId}") {
        fun createRoute(userId: Long) = "home/$userId"
    }
    object Deck : Screen("deck/{deckId}/{userId}") {
        fun createRoute(deckId: Long, userId: Long) = "deck/$deckId/$userId"
    }
    object CreateDeck : Screen("createDeck/{userId}") {
        fun createRoute(userId: Long) = "createDeck/$userId"
    }
}

@Composable
fun CogniLinkNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                onNavigateToHome = { userId ->
                    navController.navigate(Screen.Home.createRoute(userId))
                }
            )
        }
        
        composable(Screen.Terms.route) {
            val context = LocalContext.current
            val termsViewModel: TermsViewModel = viewModel(
                factory = TermsViewModel.provideFactory(context)
            )
            TermsScreen(
                onBackClick = { navController.popBackStack() },
                termsViewModel = termsViewModel
            )
        }
        
        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            ProfileScreen(userId = userId,
                onBackClick = { navController.popBackStack() },)
        }
        
        composable(
            route = Screen.Deck.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                },
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            DeckScreen(deckId = deckId, userId = userId,
                onBackClick = { navController.popBackStack() },)
        }

        composable(
            route = Screen.CreateDeck.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
//            CreateDeckScreen(
//                userId = userId,
//                onBackClick = { navController.popBackStack() }
//            )
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            HomeScreen(
                userId = userId,
                onNavigateToCreateDeck = {
                    navController.navigate(Screen.CreateDeck.createRoute(userId))
                },
                onNavigateToDeck = { deckId ->
                    navController.navigate(Screen.Deck.createRoute(deckId, userId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.createRoute(userId))
                }
            )
        }
    }
}
