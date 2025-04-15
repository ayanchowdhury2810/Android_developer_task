package com.android.androiddevelopertask.ui

import androidx.navigation.NavHostController

object Destinations {
    const val HOME_SCREEN = "homeScreen"
}

class NavigationActions(
    navController: NavHostController
) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Destinations.HOME_SCREEN){
            launchSingleTop = true
            restoreState = true
        }
    }
}