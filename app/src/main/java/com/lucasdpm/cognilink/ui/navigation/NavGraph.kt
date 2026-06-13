package com.lucasdpm.cognilink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasdpm.cognilink.ui.screens.AuthScreen
import com.lucasdpm.cognilink.ui.screens.TermsScreen
import com.lucasdpm.cognilink.ui.screens.DeckEditorScreen
import com.lucasdpm.cognilink.ui.screens.DeckScreen
import com.lucasdpm.cognilink.ui.screens.CreateFlashcardWithIAScreen
import com.lucasdpm.cognilink.ui.screens.FlashcardEditorScreen
import com.lucasdpm.cognilink.ui.screens.StudySessionScreen
import com.lucasdpm.cognilink.ui.screens.HomeScreen
import com.lucasdpm.cognilink.ui.screens.ProfileScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Terms : Screen("terms")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }

    object Home : Screen("home/{userId}") {
        fun createRoute(userId: String) = "home/$userId"
    }

    object Deck : Screen("deck/{deckId}/{userId}") {
        fun createRoute(deckId: String, userId: String) = "deck/$deckId/$userId"
    }

    object CreateDeck : Screen("createDeck/{userId}") {
        fun createRoute(userId: String) = "createDeck/$userId"
    }

    object EditDeck : Screen("editDeck/{deckId}/{userId}") {
        fun createRoute(deckId: String, userId: String) = "editDeck/$deckId/$userId"
    }

    object CreateFlashcardWithIA: Screen("createFlashcardWithIA/{deckId}"){
        fun createRoute(deckId: String) = "createFlashcardWithIA/$deckId"
    }

    object CreateFlashcardManually: Screen("createFlashcardManually/{deckId}"){
        fun createRoute(deckId: String) = "createFlashcardManually/$deckId"
    }

    object EditFlashcard: Screen("editFlashcard/{deckId}/{flashcardId}"){
        fun createRoute(deckId: String, flashcardId: String) = "editFlashcard/$deckId/$flashcardId"
    }

    object PlayFlashcard : Screen("playFlashcard/{studyMode}/{contextId}") {
        fun createRoute(studyMode: String, contextId: String) = "playFlashcard/$studyMode/$contextId"
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
            TermsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Deck.route) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DeckScreen(
                deckId = deckId, userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = {
                    navController.navigate(Screen.EditDeck.createRoute(deckId, userId))
                },
                onNavigateToStudy = { dId ->
                    navController.navigate(Screen.PlayFlashcard.createRoute("DECK", dId))
                },
                onNavigateToCreateFlashcard = { dId ->
                    navController.navigate(Screen.CreateFlashcardManually.createRoute(dId))
                },
                onNavigateToCreateWithIA = { dId ->
                    navController.navigate(Screen.CreateFlashcardWithIA.createRoute(dId))
                },
                onNavigateToFlashcard = { flashcardId ->
                    navController.navigate(Screen.PlayFlashcard.createRoute("FLASHCARD", flashcardId))
                }
            )
        }

        composable(Screen.CreateDeck.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DeckEditorScreen(
                userId = userId, deckId = null,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateFlashcard = { dId ->
                    dId?.let { navController.navigate(Screen.CreateFlashcardManually.createRoute(it)) }
                },
                onNavigateToCreateWithIA = { dId ->
                    dId?.let { navController.navigate(Screen.CreateFlashcardWithIA.createRoute(it)) }
                },
                onNavigateToEditFlashcard = { dId, flashcardId ->
                    dId?.let { navController.navigate(Screen.EditFlashcard.createRoute(it, flashcardId)) }
                }
            )
        }

        composable(Screen.EditDeck.route) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DeckEditorScreen(
                userId = userId,
                deckId = deckId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateFlashcard = { dId ->
                    val targetId = dId ?: deckId
                    navController.navigate(Screen.CreateFlashcardManually.createRoute(targetId))
                },
                onNavigateToCreateWithIA = { dId ->
                    val targetId = dId ?: deckId
                    navController.navigate(Screen.CreateFlashcardWithIA.createRoute(targetId))
                },
                onNavigateToEditFlashcard = { dId, flashcardId ->
                    val targetId = dId ?: deckId
                    navController.navigate(Screen.EditFlashcard.createRoute(targetId, flashcardId))
                }
            )
        }

        composable(Screen.CreateFlashcardWithIA.route) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            CreateFlashcardWithIAScreen(
                deckId = deckId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateFlashcardManually.route) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            FlashcardEditorScreen(
                deckId = deckId,
                flashcardId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.EditFlashcard.route) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            val flashcardId = backStackEntry.arguments?.getString("flashcardId") ?: ""
            FlashcardEditorScreen(
                deckId = deckId,
                flashcardId = flashcardId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PlayFlashcard.route) { backStackEntry ->
            val studyMode = backStackEntry.arguments?.getString("studyMode") ?: "DECK"
            val contextId = backStackEntry.arguments?.getString("contextId") ?: ""
            StudySessionScreen(
                studyMode = studyMode,
                contextId = contextId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(
                userId = userId,
                onNavigateToCreateDeck = { uId ->
                    navController.navigate(Screen.CreateDeck.createRoute(uId))
                },
                onNavigateToDeck = { dId ->
                    navController.navigate(Screen.Deck.createRoute(dId, userId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.createRoute(userId))
                },
                onNavigateToPlay = { uId ->
                    navController.navigate(Screen.PlayFlashcard.createRoute(studyMode = "REVIEW", contextId = uId))
                }
            )
        }
    }
}
