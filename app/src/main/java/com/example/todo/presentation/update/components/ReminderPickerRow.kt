package com.example.todo.presentation.update.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.core.Utils.Utils

@Composable
fun ReminderPickerRow(
    remindDate: Long,
    remindTime: Long,
    onOpenDateTimePicker: () -> Unit,
    onClearReminder: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { onOpenDateTimePicker() }
            .height(56.dp)
    ) {
        Icon(Icons.Default.Notifications, contentDescription = null)

        Spacer(modifier = Modifier.width(24.dp))

        val reminderText = if (remindDate != 0L && remindTime != 0L) {
            "${Utils.convertLongToDate(remindDate)} at ${Utils.convertLongToTime(remindTime)}"
        } else {
            "Set Reminder"
        }

        Text(
            text = reminderText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        if (remindDate != 0L && remindTime != 0L) {
            IconButton(
                onClick = onClearReminder,
            ) {
                Icon(Icons.Default.Close, contentDescription = "Remove reminder")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}