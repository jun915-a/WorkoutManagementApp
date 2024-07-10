package com.example.workoutmanagementapp.ui.dataclass

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.example.workoutmanagementapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutMenu {
    companion object {
        var bicepsMenu: MutableList<String> =
            mutableListOf("プリーチャカール", "ダンベルカール", "ハンマーカール", "ケーブルカール")
        var tricepsMenu: MutableList<String> =
            mutableListOf("フレンチプレス", "ディップス", "キックバック", "ケーブルトライセップスEXT")
        var shoulderMenu: MutableList<String> = mutableListOf("サイドレイズ", "ショルダープレス")
        var chestMenu: MutableList<String> =
            mutableListOf(
                "ベンチプレス",
                "ダンベルフライ",
                "ダンベルプレス",
                "チェストプレス",
                "ケーブルクロスオーバー",
                "ペックフライ",
                "インクラインチェストプレス",
                "デクラインチェストプレス"
            )
        var abdominalMenu: MutableList<String> = mutableListOf(
            "立ちコロ",
            "膝コロ",
            "レッグレイズ",
            "バイシクルクランチ",
            "プランク"
        )
        var backMenu: MutableList<String> =
            mutableListOf("懸垂", "ラットプルダウン", "ローイング", "ワンハンドロー", "ベントオーバーロー", "デッドリフト")
        var legMenu: MutableList<String> =
            mutableListOf(
                "スクワット",
                "レッグプレス",
                "レッグエクステンション",
                "レッグカール",
                "ブルガリアンスクワット",
                "ヒップアダクション",
                "ヒップアブダクション"
            )
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

        val Other = TrainingInfo("その他", Color.Black, null, mutableListOf())

        val Rest = TrainingInfo(
            "休み", Color.Black,
            android.R.drawable.ic_menu_recent_history, mutableListOf("休み")
        )

    }
}

data class TrainingMenu(
    val id: Int,
    val time: LocalDate?,
    val trainingInfo: TrainingInfo,
    val trainingDetailList: List<TrainingDetail>,
    val memo: String,
    val bodyWeight: String
)

data class TrainingMenuDatabase(
    val id: Int,
    val date: String,
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

fun generateTraining(tasks: MutableState<MutableList<TrainingMenuDatabase>>): List<TrainingMenu> =
    buildList {
        for (task in tasks.value) {
            val trainingInfo = checkParts(task.parts)
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
            val date = LocalDate.parse(task.date, inputFormatter)

            date.also { date ->
                add(
                    TrainingMenu(
                        task.id,
                        date,
                        trainingInfo,
                        task.trainingDetailList,
                        task.memo,
                        task.bodyWeight
                    )
                )
            }
        }
    }

fun checkParts(parts: String): TrainingInfo {
    return when (parts) {
        TrainingInfo.Chest.parts -> TrainingInfo.Chest
        TrainingInfo.Biceps.parts -> TrainingInfo.Biceps
        TrainingInfo.Triceps.parts -> TrainingInfo.Triceps
        TrainingInfo.Shoulder.parts -> TrainingInfo.Shoulder
        TrainingInfo.Back.parts -> TrainingInfo.Back
        TrainingInfo.Abdominal.parts -> TrainingInfo.Abdominal
        TrainingInfo.Leg.parts -> TrainingInfo.Leg
        TrainingInfo.Rest.parts -> TrainingInfo.Rest
        else -> TrainingInfo.Other
    }
}

//fun sampleGenerateTraining(): List<TrainingMenu> = buildList {
//    val currentMonth = YearMonth.now()
//
//    //現在の月をマイナス、プラスで月を決めて日付を決めている使用方法
//    currentMonth.atDay(17).also { date ->
//        add(
//            TrainingMenu(
//                date,
//                TrainingInfo.Chest,
//                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
//            )
//        )
//        add(
//            TrainingMenu(
//                date,
//                TrainingInfo.Triceps,
//                listOf(TrainingDetail("ダンベルフライ", "60", "2", "10"))
//            )
//        )
//    }
//}
