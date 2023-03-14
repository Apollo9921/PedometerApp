package com.example.pedometerapp.model

class StepsRepository(private val stepsDAO: StepsDAO) {

    suspend fun insertFirstStepOfDay(steps: Steps) {
        stepsDAO.insertFirstStepOfDay(steps)
    }

    suspend fun updateStepOfDay(steps: Steps) {
        stepsDAO.updateStepsOfDay(steps)
    }

}