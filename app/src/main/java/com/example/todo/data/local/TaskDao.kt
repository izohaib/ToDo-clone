package com.example.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

//    @Query("SELECT * FROM tasks WHERE listType = :listType ORDER BY dueDate ASC")
//    fun getTasksByType(listType: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isImportant = 1")
    fun getImportantTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate IS NOT NULL")
    fun getPlannedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>
}