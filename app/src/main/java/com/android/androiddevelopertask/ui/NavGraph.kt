package com.android.androiddevelopertask.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.androiddevelopertask.ui.screens.AddEditScreen.AddEditScreen
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreen
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreenViewModel

@Composable
fun NavGraph(
    startDestination: String = Destinations.HOME_SCREEN,
    navController: NavHostController,
    navigationActions: NavigationActions,
    homeScreenViewModel: HomeScreenViewModel
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ){
        composable(route = Destinations.HOME_SCREEN){
            HomeScreen(
                homeScreenViewModel,
                onAddClick = {
                    navigationActions.navigateToAddEditScreen()
                },
                onEditClick = {
                    navigationActions.navigateToAddEditScreen()
                }
            )
        }

        composable(route = Destinations.ADD_EDIT_SCREEN){
            AddEditScreen(viewModel = homeScreenViewModel, backPressed ={
                navController.popBackStack()
            })
        }
    }

}