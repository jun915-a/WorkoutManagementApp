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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutmanagementapp.ui.ShowEditDialog
import com.example.workoutmanagementapp.ui.TrainingDetail
import com.example.workoutmanagementapp.ui.TrainingInfo
import com.example.workoutmanagementapp.ui.TrainingMenu
import com.example.workoutmanagementapp.ui.theme.WorkoutManagementAppTheme
import com.example.workoutmanagementapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

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
                    MainScreen()
                    ShowEditDialog(context = this)
                    samplePlay()
                }
            }
        }
    }

    @Composable
    fun getTaskList(viewModel: MainViewModel = hiltViewModel()): MutableList<TrainingMenu> {
        val tasks: List<Task> by viewModel.tasks.collectAsState(initial = emptyList())
        val trainingMenuList: MutableList<TrainingMenu> = mutableListOf()
        for (i in tasks) {
            trainingMenuList.add(viewModel.fromJson(i.jsonStr))
        }
        return trainingMenuList
    }

    @Composable
    fun insertTask(obj: TrainingMenu, viewModel: MainViewModel = hiltViewModel()) {
        val jsonStr = viewModel.toJson(obj)
        viewModel.insertTask(Task(1, jsonStr))
    }

    @Composable
    fun samplePlay() {
        val obj = TrainingMenu(
            LocalDate.now(),
            TrainingInfo("parts", Color.Blue, R.drawable.abdominal, mutableListOf("ss")),
            listOf(TrainingDetail("a", 1, 1))
        )
        insertTask(obj = obj)

        val test = getTaskList()
        for (i in test) {
            println("test_logA ${i.trainingInfo.parts}  ${i}")
            for (a in i.trainingDetailList) {
                println("test_logC ${a}")
            }
        }
    }
}
