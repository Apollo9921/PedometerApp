package com.example.pedometerapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.pedometerapp.R
import com.example.pedometerapp.model.Steps
import com.example.pedometerapp.model.StepsDatabase
import com.example.pedometerapp.view.custom.*
import com.example.pedometerapp.view.theme.Black
import com.example.pedometerapp.view.theme.Green
import com.example.pedometerapp.view.theme.White

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context
private lateinit var owner: LifecycleOwner
private var historic: MutableList<Steps> = ArrayList()
private var totalSteps = mutableStateOf(0)
private var maxSteps = mutableStateOf(0)
private var totalCalories = mutableStateOf(0f)
private var maxCalories = mutableStateOf(0f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Historic() {
    owner = LocalLifecycleOwner.current
    context = LocalContext.current
    loadHistoricSteps()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        if (isLoaded.value) {
            if (historic.isNotEmpty()) {
                Scaffold(
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Green)
                                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = stringResource(
                                        id = R.string.totalSteps,
                                        formatTotalCount(totalSteps.value.toFloat())
                                    ),
                                    color = White,
                                    fontSize =
                                    if (mediaQueryWidth() <= small) {
                                        16.sp
                                    } else if (mediaQueryWidth() <= normal) {
                                        20.sp
                                    } else {
                                        24.sp
                                    },
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.padding(20.dp))
                                Text(
                                    text = stringResource(
                                        id = R.string.maxSteps,
                                        formatTotalCount(maxSteps.value.toFloat())
                                    ),
                                    color = White,
                                    fontSize =
                                    if (mediaQueryWidth() <= small) {
                                        16.sp
                                    } else if (mediaQueryWidth() <= normal) {
                                        20.sp
                                    } else {
                                        24.sp
                                    },
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Row {
                                Text(
                                    text = stringResource(
                                        id = R.string.totalCalories,
                                        formatTotalCount(totalCalories.value)
                                    ),
                                    color = White,
                                    fontSize =
                                    if (mediaQueryWidth() <= small) {
                                        16.sp
                                    } else if (mediaQueryWidth() <= normal) {
                                        20.sp
                                    } else {
                                        24.sp
                                    },
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.padding(20.dp))
                                Text(
                                    text = stringResource(
                                        id = R.string.maxCalories,
                                        formatTotalCount(
                                            maxCalories.value
                                        )
                                    ),
                                    color = White,
                                    fontSize =
                                    if (mediaQueryWidth() <= small) {
                                        16.sp
                                    } else if (mediaQueryWidth() <= normal) {
                                        20.sp
                                    } else {
                                        24.sp
                                    },
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = it.calculateTopPadding())
                    ) {
                        HistoricItems()
                    }
                }
            } else {
                Empty()
            }
        }
    }
}

private fun loadHistoricSteps() {
    StepsDatabase.getDatabase(context)
        .stepDao()
        .historicSteps(date.toString())
        .observe(owner) { result ->
            if (result.isNotEmpty()) {
                if (result.size > historic.size) {
                    for (i in 0 until result.size) {
                        historic.add(result[i])
                        totalSteps.value += result[i].steps
                        totalCalories.value += result[i].calories
                        if (result[i].steps > maxSteps.value) {
                            maxSteps.value = result[i].steps
                        }
                        if (result[i].calories > maxCalories.value) {
                            maxCalories.value = result[i].calories
                        }
                    }
                }
            }
            isLoaded.value = true
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoricItems() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        items(historic.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = historic[it].date,
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
            Spacer(modifier = Modifier.padding(10.dp))
            Card(
                shape = CutCornerShape(20.dp),
                border = BorderStroke(5.dp, Black),
                colors = CardDefaults.cardColors(
                    containerColor = Green,
                    contentColor = White,
                    disabledContainerColor = Green,
                    disabledContentColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    hoveredElevation = 20.dp,
                    pressedElevation = 20.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mediaQueryWidth() / 2)
                    .padding(start = 40.dp, end = 40.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.stepsHistoric,
                            formatTotalCount(historic[it].steps.toFloat())
                        ),
                        color = White,
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = stringResource(
                            id = R.string.caloriesHistoric,
                            formatTotalCount(historic[it].calories)
                        ),
                        color = White,
                        fontSize =
                        if (mediaQueryWidth() <= small) {
                            16.sp
                        } else if (mediaQueryWidth() <= normal) {
                            20.sp
                        } else {
                            24.sp
                        },
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                color = Green,
                thickness = 5.dp
            )
        }
    }
}

@Composable
private fun Empty() {
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
            text = stringResource(id = R.string.emptyHistoric),
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