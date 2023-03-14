package com.example.pedometerapp.view.bottomBar

import com.example.pedometerapp.R
import com.example.pedometerapp.view.navigation.Destination

sealed class BottomNavigationItems(val title: Int, var icon: Int, var route: String) {
    object Counter : BottomNavigationItems(R.string.counter, R.drawable.home, Destination.Counter.route)
    object Historic : BottomNavigationItems(R.string.historic, R.drawable.historic, Destination.Historic.route)
}
