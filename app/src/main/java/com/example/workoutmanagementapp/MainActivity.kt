package com.example.workoutmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutmanagementapp.ui.ShowEditDialog
import com.example.workoutmanagementapp.ui.TrainingDetail
import com.example.workoutmanagementapp.ui.TrainingMenuDatabase
import com.example.workoutmanagementapp.ui.theme.WorkoutManagementAppTheme
import com.example.workoutmanagementapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //tasks管理変数
                    val tasks = remember { mutableStateOf(mutableListOf<TrainingMenuDatabase>()) }

                    tasks.value = getTaskList()
                    for (i in tasks.value) {
                        println("test_log_起動時取得データ 日付：${i.date} 部位：${i.parts} 体重：${i.bodyWeight} メモ：${i.memo} ")
                        for (detail in i.trainingDetailList) {
                            println("test_log_起動時取得データ_トレーニング詳細 トレーニング：${detail.trainingName} 重量：${detail.weight} レップ：${detail.rep} セット：${detail.set}")
                        }
                    }
                    MainScreen(tasks)
                    ShowEditDialog(context = this)
                }
            }
        }
    }

    @Composable
    fun getTaskList(viewModel: MainViewModel = hiltViewModel()): MutableList<TrainingMenuDatabase> {
        val tasks: List<Task> by viewModel.tasks.collectAsState(initial = emptyList())
        val trainingMenuList: MutableList<TrainingMenuDatabase> = mutableListOf()
        for (i in tasks) {
            trainingMenuList.add(viewModel.fromJson(i.jsonStr))
        }
        return trainingMenuList
    }

    @Composable
    fun insertTask(obj: TrainingMenuDatabase, viewModel: MainViewModel = hiltViewModel()) {
        val jsonStr = viewModel.toJson(obj)
        viewModel.insertTask(Task(1, jsonStr))
    }

    @Composable
    fun samplePlay() {
        val obj = TrainingMenuDatabase(
            "2024-04-01",
            "parts",
            listOf(TrainingDetail("a", "60", "1", "1")), "", ""
        )
        insertTask(obj = obj)

        val test = getTaskList()
        for (i in test) {
            println("test_log_起動時取得データ 日付：${i.date} 部位：${i.parts} 体重：${i.bodyWeight} メモ：${i.memo} ")
            for (detail in i.trainingDetailList) {
                println("test_log_起動時取得データ_トレーニング詳細 トレーニング：${detail.trainingName} 重量：${detail.weight} レップ：${detail.rep} セット：${detail.set}")
            }
        }
    }
}
