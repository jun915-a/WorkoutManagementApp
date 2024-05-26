package com.example.workoutmanagementapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var date: String,//日付形式に変更
    var trainingParts: String,
    var trainingMenu: String,//Listにできないのでカンマ区切りで管理
    var trainingSet: String,//Listにできないのでカンマ区切りで管理
    var trainingRep: String,//Listにできないのでカンマ区切りで管理
    var memo: String,
    var weight: Double
)

data class TrainingMenuSetRep(
    var trainingMenu: String,
    var trainingSet: Int,
    var trainingRep: Int,
)