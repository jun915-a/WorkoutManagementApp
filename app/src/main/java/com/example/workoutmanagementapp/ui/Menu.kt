package com.example.workoutmanagementapp.ui

import androidx.compose.ui.graphics.Color
import com.example.workoutmanagementapp.R
import java.time.LocalDateTime
import java.time.YearMonth

enum class TrainingMenuList(
    trainingName: String,
    color: Color,
    image: Int,
    val menu: String? = null
) {
    Chest("胸", Color.Red, R.drawable.chest),

    Biceps("二頭筋", Color.Yellow, R.drawable.biceps),

    Triceps("三頭筋", Color.Magenta, R.drawable.triceps),

    Shoulder("肩", Color.Gray, R.drawable.shoulder),

    Back("背中", Color.Blue, R.drawable.back),

    Abdominal("腹筋", Color.Green, R.drawable.abdominal),

    Leg("足", Color.Cyan, R.drawable.leg),
}

data class Training(val trainingMenu: TrainingMenu, ) {
    data class TrainingMenu(
        val trainingName: String,
        val color: Color,
        val image: Int,
        val menu: String? = null,
    )
}

fun generateTraining(): List<Training> = buildList {
    val currentMonth = YearMonth.now()

    //現在の月をマイナス、プラスで月を決めて日付を決めている使用方法
    //TODO:ここをRoomで保存、管理
    //中の値を変えることで日付や表示アイテムを指定できる
    currentMonth.atDay(17).also { date ->
        add(
            Training(
                Training.TrainingMenu("胸", Color.Red, R.drawable.chest)
            ),
        )
        add(
            Training(
                Training.TrainingMenu("二頭筋", Color.Yellow, R.drawable.biceps)
            ),
        )
    }

    currentMonth.atDay(22).also { date ->
        add(
            Training(
                Training.TrainingMenu("三頭筋", Color.Magenta, R.drawable.triceps)
            ),
        )
        add(
            Training(
                Training.TrainingMenu("三頭筋", Color.Magenta, R.drawable.triceps)
            ),
        )
    }

    currentMonth.atDay(3).also { date ->
        add(
            Training(
                Training.TrainingMenu("背中", Color.Blue, R.drawable.back)
            ),
        )
    }

    currentMonth.atDay(12).also { date ->
        add(
            Training(
                Training.TrainingMenu("足", Color.Cyan, R.drawable.leg)
            ),
        )
    }

    currentMonth.plusMonths(1).atDay(13).also { date ->
        add(
            Training(
                Training.TrainingMenu("腹筋", Color.Green, R.drawable.abdominal)
            ),
        )
        add(
            Training(
                Training.TrainingMenu("背中", Color.Blue, R.drawable.back)
            ),
        )
    }

    currentMonth.minusMonths(1).atDay(9).also { date ->
        add(
            Training(
                Training.TrainingMenu("背中", Color.Blue, R.drawable.back)
            ),
        )
    }
}