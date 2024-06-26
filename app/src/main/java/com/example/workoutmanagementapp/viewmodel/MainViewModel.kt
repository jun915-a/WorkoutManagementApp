package com.example.workoutmanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutmanagementapp.room.Task
import com.example.workoutmanagementapp.room.TaskDao
import com.example.workoutmanagementapp.ui.dataclass.TrainingMenu
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    //新規アイテム追加
    var showEditDialogFlg by mutableStateOf(false)

    //既存アイテム編集
    var showChangeDialogFlg by mutableStateOf(false)

    //既存アイテム編集時データ
    var trainingMenu: TrainingMenu? = null


//    var showDeleteDialogFlg by mutableStateOf(false)
//    var showProgressBarFlg by mutableStateOf(false)

    var currentMaxId by mutableIntStateOf(0)
    var memo by mutableStateOf("")
    var bodyWeight by mutableStateOf("")
    var day by mutableStateOf("")
    var dataBaseDay by mutableStateOf("")

    var parts by mutableStateOf("")
    var trainingName: MutableList<String> = mutableListOf()
    var weight: MutableList<String> = mutableListOf()
    var rep: MutableList<String> = mutableListOf()
    var set: MutableList<String> = mutableListOf()


    /*
     * アプリ起動時taskを全件取得
     */
    val tasks = taskDao.loadAllTasks().distinctUntilChanged()

    /*
     * 保存ボタン押下時taskを保存
     */
    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
            println("success_test_log ${task.jsonStr} }")
        }
    }

    /*
     * 編集モードから保存した際のアップデート
     */
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

    /*
     * TODO　カレンダー下部のアイテム長押しで削除
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
//            Log.d("delete_updateTask", "${task.id} ${task.name} ${task.userId} ${task.password}")
        }
    }

    // オブジェクトをJSON文字列に変換する
    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    // JSON文字列をオブジェクトに変換する
    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }
}