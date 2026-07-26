package com.naufal.mynote.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object About : Screen("about")
    data object Editor : Screen("editor?noteId={noteId}") {
        const val ARG_NOTE_ID = "noteId"
        const val NO_ID = -1L
        fun buildRoute(noteId: Long = NO_ID): String = "editor?noteId=$noteId"
    }
}
