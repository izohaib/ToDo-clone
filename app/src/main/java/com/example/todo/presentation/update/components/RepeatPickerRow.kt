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
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepeatPickerRow(
    repeatInterval: Int,
    repeatUnit: String,
    selectedDays: List<String>,
    onOpenRepeatDialog: () -> Unit,
    onClearRepeat: () -> Unit
) {
    val text = if (selectedDays.isEmpty()) {
        "Repeat"
    } else {
        val previewDays = selectedDays.take(3)
        val preview = previewDays.joinToString()
        val suffix = if (selectedDays.size > 3) "…" else ""
        "Weekly $repeatInterval $repeatUnit on $preview$suffix"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 24.dp, end = 24.dp)
            .clickable { onOpenRepeatDialog() }
    ) {
        Icon(
            imageVector = Icons.Default.Repeat,
            contentDescription = "Repeat"
        )

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        if (selectedDays.isNotEmpty()) {
            IconButton(onClick = onClearRepeat) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Repeat"
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}