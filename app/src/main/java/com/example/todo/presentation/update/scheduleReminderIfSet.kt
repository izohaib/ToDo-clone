package com.example.todo.presentation.update

import android.content.Context
import com.example.todo.core.AlarmManager.NotificationScheduler
import com.example.todo.data.local.Task
import java.util.Calendar

fun scheduleReminderIfSet(task: Task, context: Context) {

    if (task.reminderTime != null && task.reminderDate != null) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = task.reminderDate
            val timeCal = Calendar.getInstance().apply {
                timeInMillis = task.reminderTime
            }
            set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
        }

        NotificationScheduler.scheduleReminder(
            context = context,
            taskId = task.id,
            title = task.title,
//            description = task.note ?: "",
            timeInMillis = calendar.timeInMillis
        )
    }
}