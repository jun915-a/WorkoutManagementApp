package com.example.workoutmanagementapp.ui.dataclass

data class WorkoutMenu(
    var bicepsMenu: MutableList<String> = mutableListOf("ダンベルカール", "ハンマーカール"),
    var tricepsMenu: MutableList<String> = mutableListOf("フレンチプレス", "ディップス","キックバック"),
    var shoulderMenu: MutableList<String> = mutableListOf("サイドレイズ", "ショルダープレス"),
    var chestMenu: MutableList<String> = mutableListOf("ダンベルフライ", "ダンベルプレス","チェストプレス","ベンチプレス"),
    var abdominalMenu: MutableList<String> = mutableListOf("立ちコロ", "膝コロ","レッグレイズ","バイシクルクランチ","プランク"),
    var backMenu: MutableList<String> = mutableListOf("懸垂", "ラットプルダウン"),
    var quadricepsMenu: MutableList<String> = mutableListOf("スクワット", "レッグプレス","レッグエクステンション"),
    var hamstringMenu: MutableList<String> = mutableListOf("レッグカール", "ブルガリアンスクワット"),
)
