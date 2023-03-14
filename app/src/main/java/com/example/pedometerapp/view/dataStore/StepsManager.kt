package com.example.pedometerapp.view.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStoreSteps: DataStore<Preferences> by preferencesDataStore(name = "steps_prefs")

class StepsManager(val dataStore: DataStore<Preferences>) {

    companion object {
        val STEP_COUNT_KEY = intPreferencesKey("STEP_COUNT")
        val CALORIES_BURNED_KEY = floatPreferencesKey("CALORIES_BURNED")
        val DATE_KEY = stringPreferencesKey("DATE_KEY")
    }

    suspend fun storeToDataStore(stepCounter: Int, caloriesBurned: Float, date: String) {
        dataStore.edit {
            it[STEP_COUNT_KEY] = stepCounter
            it[CALORIES_BURNED_KEY] = caloriesBurned
            it[DATE_KEY] = date
        }
    }

    val stepsFlow: Flow<Int> = dataStore.data.map {
        if (it[STEP_COUNT_KEY] != null) {
            it[STEP_COUNT_KEY]
        } else {
            0
        }!!
    }

    val caloriesFlow: Flow<Float> = dataStore.data.map {
        (if (it[CALORIES_BURNED_KEY] != null) {
            it[CALORIES_BURNED_KEY]
        } else {
            0f
        })!!
    }

    val dateFlow: Flow<String> = dataStore.data.map {
        if (it[DATE_KEY].toString() != "null") {
            it[DATE_KEY].toString()
        } else {
            ""
        }
    }
}