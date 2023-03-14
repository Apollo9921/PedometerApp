package com.example.pedometerapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps_table")
data class Steps(
    @PrimaryKey(autoGenerate = false)
    val date: String,
    val steps: Int,
    val calories: Float,
)
