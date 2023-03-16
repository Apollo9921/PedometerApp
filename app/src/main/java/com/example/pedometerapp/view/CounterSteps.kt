package com.example.pedometerapp.view

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.example.pedometerapp.R
import com.example.pedometerapp.model.Steps
import com.example.pedometerapp.view.custom.mediaQueryWidth
import com.example.pedometerapp.view.custom.normal
import com.example.pedometerapp.view.custom.small
import com.example.pedometerapp.view.theme.Black
import com.example.pedometerapp.view.theme.Green
import com.example.pedometerapp.view.theme.White
import com.example.pedometerapp.viewModel.CounterStepsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private var counterSteps = mutableStateOf(0)
private var calories = mutableStateOf(0f)
private var noSensorExists = mutableStateOf(false)
private lateinit var counterStepsViewModel: CounterStepsViewModel

@Composable
fun CounterSteps() {
    counterStepsViewModel = CounterStepsViewModel(LocalContext.current)
    var dateSaved: String
    runBlocking {
        counterSteps.value = stepsManager.stepsFlow.first()
        calories.value = stepsManager.caloriesFlow.first().toFloat()
        dateSaved = stepsManager.dateFlow.first().toString()
    }
    if (dateSaved.isNotBlank() && dateSaved != date.toString()) {
        val stepsUpdate = Steps(dateSaved, counterSteps.value, calories.value)
        counterStepsViewModel.updateStepOfDay(stepsUpdate)
        counterSteps.value = 0
        calories.value = 0f
        dateSaved = ""
        runBlocking { stepsManager.dataStore.edit { it.clear() } }
    }
    if (counterSteps.value >= 1) {
        val steps = Steps(date.toString(), counterSteps.value, calories.value)
        counterStepsViewModel.insertFirstStepOfDay(steps)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        if (!noSensorExists.value) {
            Column(
                modifier = Modifier
                    .size(mediaQueryWidth() - 50.dp)
                    .border(5.dp, Green, RoundedCornerShape(50.dp))
                    .padding(start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.counterSteps, counterSteps.value),
                    color = Green,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        36.sp
                    } else if (mediaQueryWidth() <= normal) {
                        40.sp
                    } else {
                        44.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = stringResource(id = R.string.counterCalories, calories.value),
                    color = Green,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        26.sp
                    } else if (mediaQueryWidth() <= normal) {
                        30.sp
                    } else {
                        34.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Black),
                    modifier = Modifier
                        .size(
                            if (mediaQueryWidth() <= small) {
                                100.dp
                            } else if (mediaQueryWidth() <= normal) {
                                150.dp
                            } else {
                                200.dp
                            }
                        )
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = stringResource(id = R.string.noSensorExists),
                    color = Black,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        26.sp
                    } else if (mediaQueryWidth() <= normal) {
                        30.sp
                    } else {
                        34.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    StepSensor()
}

@Composable
private fun StepSensor() {
    var inStep = false
    val ctx = LocalContext.current

    val sensorManager: SensorManager = ctx.getSystemService(SENSOR_SERVICE) as SensorManager

    val stepSensor: Sensor? = if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    } else {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            null
        }
    }


    if (stepSensor != null) {
        val stepSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    counterSteps.value++
                    calories.value = (counterSteps.value * 0.04).toFloat()
                    runBlocking {
                        stepsManager.dataStore.edit { it.clear() }
                        stepsManager.storeToDataStore(
                            counterSteps.value,
                            calories.value,
                            date.toString()
                        )
                    }
                }
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val currentVectorSum = x * x + y * y + z * z
                    if (currentVectorSum < 100 && !inStep) {
                        inStep = true
                    }
                    if (currentVectorSum > 225 && currentVectorSum < 275 && inStep) {
                        inStep = false
                        counterSteps.value++
                        calories.value = (counterSteps.value * 0.04).toFloat()
                        runBlocking {
                            stepsManager.dataStore.edit { it.clear() }
                            stepsManager.storeToDataStore(
                                counterSteps.value,
                                calories.value,
                                date.toString()
                            )
                        }
                    }
                }
            }
        }

        sensorManager.registerListener(
            stepSensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    } else {
        noSensorExists.value = true
    }
}