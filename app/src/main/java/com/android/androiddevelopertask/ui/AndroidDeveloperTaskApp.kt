package com.android.androiddevelopertask.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.android.androiddevelopertask.ui.theme.AndroidDeveloperTaskTheme

@Composable
fun AndroidDeveloperTaskApp(
    navController: NavHostController,
    navigationActions: NavigationActions
) {
    AndroidDeveloperTaskTheme {
        NavGraph(
            navController = navController,
            navigationActions = navigationActions
        )
    }

}