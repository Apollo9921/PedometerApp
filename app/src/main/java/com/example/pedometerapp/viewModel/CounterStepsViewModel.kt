package com.example.pedometerapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometerapp.model.Steps
import com.example.pedometerapp.model.StepsDatabase
import com.example.pedometerapp.model.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CounterStepsViewModel(application: Context): ViewModel() {

    private val repository: StepsRepository

    init {
        val stepsDao = StepsDatabase.getDatabase(application).stepDao()
        repository = StepsRepository(stepsDao)
    }

    fun insertFirstStepOfDay(steps: Steps) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFirstStepOfDay(steps)
        }
    }

    fun updateStepOfDay(steps: Steps) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStepOfDay(steps)
        }
    }
}