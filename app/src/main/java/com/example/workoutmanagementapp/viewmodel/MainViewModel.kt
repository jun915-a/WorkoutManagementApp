package com.example.workoutmanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutmanagementapp.Task
import com.example.workoutmanagementapp.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    //TODO このApp仕様にする

    var showDeleteDialogFlg by mutableStateOf(false)
    var showEditDialogFlg by mutableStateOf(false)
    var showProgressBarFlg by mutableStateOf(false)

    var id by mutableIntStateOf(0)
    var memo by mutableStateOf("")
    var weight by mutableStateOf("")
    var day by mutableStateOf("")

    val tasks = taskDao.loadAllTasks().distinctUntilChanged()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
//            Log.d("success_insertTask", "${task.id} ${task.name} ${task.userId} ${task.password}")
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
//            Log.d("success_updateTask", "${task.id} ${task.name} ${task.userId} ${task.password}")
        }
    }

    fun updateIdsAfterDelete(taskId: Int) {
        viewModelScope.launch {
            taskDao.updateIdsAfterDelete(taskId)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
//            Log.d("delete_updateTask", "${task.id} ${task.name} ${task.userId} ${task.password}")
        }
    }
}