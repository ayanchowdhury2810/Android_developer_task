package com.android.androiddevelopertask.ui

import androidx.navigation.NavHostController

object Destinations {
    const val HOME_SCREEN = "homeScreen"
    const val ADD_EDIT_SCREEN = "addEditScreen"
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

    val navigateToAddEditScreen: () -> Unit = {
        navController.navigate(Destinations.ADD_EDIT_SCREEN){
            launchSingleTop = true
            restoreState = true
        }
    }
}