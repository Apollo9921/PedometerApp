package com.example.pedometerapp.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.pedometerapp.view.navigation.NavigationGraph
import com.example.pedometerapp.view.theme.PedometerAppTheme

var keepSplashOpened = true
@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        setContent {
            PedometerAppTheme {
                context = LocalContext.current
                val navController = rememberNavController()
                NavigationGraph(navHostController = navController)
            }
        }
    }
}