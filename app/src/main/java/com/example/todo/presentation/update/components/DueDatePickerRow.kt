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
import androidx.compose.material.icons.filled.DateRange
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
fun DueDatePickerRow(
    dueDate: Long,
    onOpenDatePicker: () -> Unit,
    onClearDate: () -> Unit
) {
    val formattedDate = if (dueDate != 0L) {
        Utils.convertLongToDate(dueDate)
    } else {
        "Set Due Date"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDatePicker() }
            .height(56.dp)
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Due Date"
        )

        Spacer(modifier = Modifier.width(24.dp)) // spacing after icon

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        if (dueDate != 0L) {
            IconButton(
                onClick = onClearDate,

            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Date"
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp)) // spacing at the end for alignment
    }
}