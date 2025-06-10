package com.example.todo.data.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dueDate: Long?,
    val reminderDate: Long?,
    val reminderTime: Long?,
    val repeatInterval: Int?,
    val repeatUnit: String?,
    val repeatDays: List<String>?, // Type converter required
    val isCompleted: Boolean = false,
    val isImportant: Boolean = false,
    val isMyDay: Boolean = false,
    val steps: List<Step> = emptyList(),// (needs converter)
    val note: String? = null,
    val fileUri: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class Step(
    val title: String,
    val isCompleted: Boolean = false,
)