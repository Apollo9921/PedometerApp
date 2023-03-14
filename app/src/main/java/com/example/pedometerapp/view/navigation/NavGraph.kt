package com.example.pedometerapp.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pedometerapp.view.CounterSteps
import com.example.pedometerapp.view.Historic
import com.example.pedometerapp.view.Main

@Composable
fun NavigationGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Destination.Main.route) {
        composable(
            route = Destination.Main.route
        ) {
            Main()
        }
        composable(
            route = Destination.Counter.route
        ) {
            CounterSteps()
        }
        composable(
            route = Destination.Historic.route
        ) {
            Historic()
        }
    }
}