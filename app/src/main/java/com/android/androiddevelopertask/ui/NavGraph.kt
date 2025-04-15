package com.android.androiddevelopertask.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreen

@Composable
fun NavGraph(
    startDestination: String = Destinations.HOME_SCREEN,
    navController: NavHostController,
    navigationActions: NavigationActions
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ){
        composable(route = Destinations.HOME_SCREEN){
            HomeScreen()
        }
    }

}