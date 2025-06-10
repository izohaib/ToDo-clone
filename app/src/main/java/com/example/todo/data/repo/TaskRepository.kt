package com.example.todo.data.repo

import com.example.todo.data.local.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
//    fun getTasksByType(type: String): Flow<List<Task>> //getting in the myday screen
//    fun getImportantTasks(): Flow<List<Task>>
//    fun getPlannedTasks(): Flow<List<Task>>
//    fun getCompletedTasks(): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
}