package com.example.todo.presentation.myDay

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.Task
import com.example.todo.data.repo.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyDayViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun toggleImportant(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isImportant = !task.isImportant))
        }
    }

    fun addTaskWithId(task: Task, onTaskInserted: (Task) -> Unit) {
        viewModelScope.launch {
            try {
                val insertedId = repository.insertTask(task) // Uses the new method
                val taskWithId = task.copy(id = insertedId.toInt())
                onTaskInserted(taskWithId)
                Log.d("MyDayViewModel", "Task inserted with ID: $insertedId")
            } catch (e: Exception) {
                Log.e("MyDayViewModel", "Failed to insert task", e)
            }
        }
    }


    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun getTaskById(id: Int): Task? {
        return allTasks.value.find { it.id == id }
    }
}

//@HiltViewModel
//class MyDayViewModel @Inject constructor(
//    private val repository: TaskRepository
//) : ViewModel() {
//
////    val myDayTasks = repository.getTasksByType("MY_DAY")
////        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//
//    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )
//
//    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
//    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
//
//
//    fun addTask(task: Task) {
//        viewModelScope.launch {
//            repository.insertTask(task)
//        }
//    }
//
//    fun updateTask(task: Task) {
//        viewModelScope.launch {
//            repository.updateTask(task)
//        }
//    }
//
//
//    fun toggleComplete(task: Task) {
//        viewModelScope.launch {
//            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
//        }
//    }
//
//    fun toggleImportant(task: Task) {
//        viewModelScope.launch {
//            repository.updateTask(task.copy(isImportant = !task.isImportant))
//        }
//    }
//
//    fun deleteTask(task: Task) {
//        viewModelScope.launch {
//            repository.deleteTask(task)
//        }
//    }
//
//    fun getTaskById(id: Int): Task? {
//        return _tasks.value.find { it.id == id }
//    }
//
//
//}
//