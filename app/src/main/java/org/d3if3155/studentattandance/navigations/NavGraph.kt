package org.d3if3155.studentattandance.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3155.studentattandance.screen.MainScreen
import org.d3if3155.studentattandance.screen.PresenceScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen"
    ){
        composable("mainScreen"){
            MainScreen(navController)
        }
        composable("presenceScreen"){
            PresenceScreen(navController)
        }

    }
}