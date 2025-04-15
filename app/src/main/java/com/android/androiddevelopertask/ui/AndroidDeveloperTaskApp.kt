package com.android.androiddevelopertask.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreenViewModel
import com.android.androiddevelopertask.ui.theme.AndroidDeveloperTaskTheme

@Composable
fun AndroidDeveloperTaskApp(
    navController: NavHostController,
    navigationActions: NavigationActions,
    homeScreenViewModel: HomeScreenViewModel
) {
    AndroidDeveloperTaskTheme {
        NavGraph(
            navController = navController,
            navigationActions = navigationActions,
            homeScreenViewModel = homeScreenViewModel
        )
    }

}