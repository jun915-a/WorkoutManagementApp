package com.example.workoutmanagementapp.ui

import androidx.compose.ui.graphics.Color
import com.example.workoutmanagementapp.R
import java.time.LocalDate
import java.time.YearMonth

class WorkoutMenu {
    companion object {
        var bicepsMenu: MutableList<String> = mutableListOf("ダンベルカール", "ハンマーカール")
        var tricepsMenu: MutableList<String> = mutableListOf("フレンチプレス", "ディップス", "キックバック")
        var shoulderMenu: MutableList<String> = mutableListOf("サイドレイズ", "ショルダープレス")
        var chestMenu: MutableList<String> =
            mutableListOf("ダンベルフライ", "ダンベルプレス", "チェストプレス", "ベンチプレス")
        var abdominalMenu: MutableList<String> = mutableListOf(
            "立ちコロ",
            "膝コロ",
            "レッグレイズ",
            "バイシクルクランチ",
            "プランク"
        )
        var backMenu: MutableList<String> = mutableListOf("懸垂", "ラットプルダウン")
        var legMenu: MutableList<String> =
            mutableListOf("スクワット", "レッグプレス", "レッグエクステンション", "レッグカール", "ブルガリアンスクワット")
    }
}

data class TrainingInfo(
    val parts: String,
    val color: Color?,
    val image: Int?,
    val workoutMenu: MutableList<String>,
) {
    companion object {
        val Chest = TrainingInfo("胸", Color.Red, R.drawable.chest, WorkoutMenu.chestMenu)

        val Biceps = TrainingInfo("二頭筋", Color.Yellow, R.drawable.biceps, WorkoutMenu.bicepsMenu)

        val Triceps =
            TrainingInfo("三頭筋", Color.Magenta, R.drawable.triceps, WorkoutMenu.tricepsMenu)

        val Shoulder =
            TrainingInfo("肩", Color.White, R.drawable.shoulder, WorkoutMenu.shoulderMenu)

        val Back = TrainingInfo("背中", Color.Blue, R.drawable.back, WorkoutMenu.backMenu)

        val Abdominal =
            TrainingInfo("腹筋", Color.Green, R.drawable.abdominal, WorkoutMenu.abdominalMenu)

        val Leg = TrainingInfo("足", Color.Cyan, R.drawable.leg, WorkoutMenu.legMenu)
    }
}

data class TrainingMenu(
    val time: LocalDate?,
    val trainingInfo: TrainingInfo,
    val trainingDetailList: List<TrainingDetail>,
)

data class TrainingMenuDatabase(
    val time: String,
    val parts: String,
    val trainingDetailList: List<TrainingDetail>,
    val memo: String,
    val bodyWeight: String
)

data class TrainingDetail(
    val trainingName: String,
    val weight: String,
    val set: String,
    val rep: String
)


fun generateTraining(): List<TrainingMenu> = buildList {
    val currentMonth = YearMonth.now()

    //現在の月をマイナス、プラスで月を決めて日付を決めている使用方法
    //TODO:ここをRoomで保存、管理
    //中の値を変えることで日付や表示アイテムを指定できる
    currentMonth.atDay(17).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Chest,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
        add(
            TrainingMenu(
                date,
                TrainingInfo.Triceps,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }

    currentMonth.atDay(22).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Back,
                listOf(
                    TrainingDetail("ダンベルフライ", "60", "2", "10"),
                    TrainingDetail("ダンベルフライ", "60", "2", "10"),
                    TrainingDetail("ダンベルフライ", "60", "2", "10"),
                    TrainingDetail("ダンベルフライ", "60", "2", "10"),
                )
            )
        )

        add(
            TrainingMenu(
                date,
                TrainingInfo.Biceps,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }

    currentMonth.atDay(3).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Abdominal,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }

    currentMonth.atDay(12).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Shoulder,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }

    currentMonth.plusMonths(1).atDay(13).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Leg,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
        add(
            TrainingMenu(
                date,
                TrainingInfo.Triceps,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }

    currentMonth.minusMonths(1).atDay(9).also { date ->
        add(
            TrainingMenu(
                date,
                TrainingInfo.Triceps,
                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
            )
        )
    }
}

val myTrainingDetailList: MutableList<TrainingDetail> = mutableListOf()

fun makeTrainingDetail(
    workoutMenu: String,
    weight: String,
    set: String,
    rep: String
): MutableList<TrainingDetail> {
    myTrainingDetailList.add(TrainingDetail(workoutMenu, weight, set, rep))
    return myTrainingDetailList
}