package com.example.workoutmanagementapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var userId: String,
    var password: String,
    var memo: String,
)
