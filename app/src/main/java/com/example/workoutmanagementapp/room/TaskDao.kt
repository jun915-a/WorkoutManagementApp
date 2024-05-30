package com.example.workoutmanagementapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workoutmanagementapp.room.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    /*
     * タスク一つ分のデータをDBに保存
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /*
     * DBに保存されているすべてのタスクを取得
     */
    @Query("SELECT * FROM Task")
    fun loadAllTasks(): Flow<List<Task>>

    /*
     * DBに保存されているタスクを更新する(同じprimaryKeyのタスクを更新する)
     */
    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE Task SET id = id - 1 WHERE id > :deletedId")
    suspend fun updateIdsAfterDelete(deletedId: Int)

    /*
     * DBに保存されているタスクを削除する
     */
    @Delete
    suspend fun deleteTask(task: Task)

}
