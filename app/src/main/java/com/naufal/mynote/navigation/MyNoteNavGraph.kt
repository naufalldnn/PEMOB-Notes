package com.naufal.mynote.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naufal.mynote.ui.screens.DashboardScreen
import com.naufal.mynote.ui.screens.EditorScreen
import com.naufal.mynote.viewmodel.NoteViewModel

@Composable
fun MyNoteNavGraph() {
    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = noteViewModel,
                onAddNote = {
                    navController.navigate(Screen.Editor.buildRoute())
                },
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Editor.buildRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                navArgument(Screen.Editor.ARG_NOTE_ID) {
                    type = NavType.LongType
                    defaultValue = Screen.Editor.NO_ID
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments
                ?.getLong(Screen.Editor.ARG_NOTE_ID) ?: Screen.Editor.NO_ID

            EditorScreen(
                viewModel = noteViewModel,
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
