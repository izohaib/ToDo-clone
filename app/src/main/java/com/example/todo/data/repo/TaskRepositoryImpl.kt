package com.example.todo.data.repo

import com.example.todo.data.local.Task
import com.example.todo.data.local.TaskDao
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {
    override suspend fun insertTask(task: Task): Long = dao.insertTask(task)
    override suspend fun updateTask(task: Task) = dao.updateTask(task)
    override suspend fun deleteTask(task: Task) = dao.deleteTask(task)
//    override fun getTasksByType(type: String) = dao.getTasksByType(type)
//    override fun getImportantTasks() = dao.getImportantTasks()
//    override fun getPlannedTasks() = dao.getPlannedTasks()
//    override fun getCompletedTasks() = dao.getCompletedTasks()
    override fun getAllTasks() = dao.getAllTasks()
}