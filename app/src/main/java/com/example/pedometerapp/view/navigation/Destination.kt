package com.example.pedometerapp.view.navigation

sealed class Destination(val route: String) {
    object Main: Destination(route = "main")
    object Counter: Destination(route = "counter")
    object Historic: Destination(route = "historic")
}
