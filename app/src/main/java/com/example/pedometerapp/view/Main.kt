package com.example.pedometerapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.pedometerapp.view.bottomBar.BottomNavigationItems
import com.example.pedometerapp.view.bottomBar.bottomNavigationBar
import com.example.pedometerapp.view.custom.RequestPermission
import com.example.pedometerapp.view.custom.isLoaded
import com.example.pedometerapp.view.dataStore.StepsManager
import com.example.pedometerapp.view.dataStore.dataStoreSteps
import com.example.pedometerapp.view.main.keepSplashOpened
import com.example.pedometerapp.view.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDate

lateinit var stepsManager: StepsManager
lateinit var date: LocalDate
private var index = mutableStateOf("")

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Main() {
    keepSplashOpened = false
    date = remember {
        LocalDate.now()
    }
    stepsManager = StepsManager(dataStore = LocalContext.current.dataStoreSteps)
    Scaffold(
        bottomBar = { index.value = bottomNavigationBar() }
    ) {
        RequestPermission()
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rememberPermissionState(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            rememberPermissionState("")
        }
        if (permission.permission.isNotBlank() && permission.status.isGranted) {
            Box(
                modifier = Modifier
                    .background(White)
                    .padding(bottom = it.calculateBottomPadding())
            ) {
                when (index.value) {
                    BottomNavigationItems.Counter.route -> {
                        isLoaded.value = false
                        CounterSteps()
                    }
                    BottomNavigationItems.Historic.route -> {
                        Historic()
                    }
                }
            }
        }
    }
}