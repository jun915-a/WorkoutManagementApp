package com.example.workoutmanagementapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var date: String,//日付形式に変更
    var trainingParts: String,
    var trainingMenuSetRep: Triple<String, Int, Int>,
    var memo: String,
    var weight: Float
)
