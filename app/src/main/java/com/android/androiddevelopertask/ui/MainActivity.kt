package com.android.androiddevelopertask.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import androidx.work.WorkManager
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsGranted ->
                permissionsGranted.forEach { (permission, granted) ->
                    if (granted) {
                        Log.d("permission", "$permission granted")

                    } else {
                        Log.d("permission", "$permission denied")
                    }
                }
            }

            val permissions = listOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.POST_NOTIFICATIONS
            )


            LaunchedEffect(Unit) {
                permissionLauncher.launch(permissions.toTypedArray())
            }

            val navController = rememberNavController()
            val navigationActions = remember(navController){
                NavigationActions(navController)
            }

            AndroidDeveloperTaskApp(
                navController,
                navigationActions,
                homeScreenViewModel
            )
        }
    }
}