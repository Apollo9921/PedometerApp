package com.example.pedometerapp.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StepsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFirstStepOfDay(steps: Steps)

    @Update
    suspend fun updateStepsOfDay(steps: Steps)

    @Query("SELECT * FROM steps_table WHERE date != :date ORDER BY date DESC")
    fun historicSteps(date: String): LiveData<List<Steps>>

}