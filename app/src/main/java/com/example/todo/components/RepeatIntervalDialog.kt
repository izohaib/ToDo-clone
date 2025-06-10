package com.example.todo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.todo.presentation.myDay.DayToggle


@Composable
fun RepeatIntervalDialog(
    repeatInterval: Int,
    onRepeatIntervalChange: (Int) -> Unit,
    repeatUnit: String,
    onRepeatUnitChange: (String) -> Unit,
    selectedDays: List<String>,
    onToggleDay: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val daysOfWeek = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column (
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Repeat every...", style = MaterialTheme.typography.titleMedium)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = repeatInterval.toString(),
                        onValueChange = {
                            onRepeatIntervalChange(it.toIntOrNull() ?: 1)
                        },
                        modifier = Modifier.width(80.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    DropdownMenuBox(repeatUnit, listOf("days", "weeks", "months")) {
                        onRepeatUnitChange(it)
                    }
                }

                Spacer(Modifier.height(12.dp))

                FlowRow(horizontalArrangement = Arrangement.Center) {
                    daysOfWeek.forEach { day ->
                        DayToggle(
                            day = day,
                            selected = selectedDays.contains(day),
                            onToggle = { onToggleDay(day) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("CANCEL") }
                    TextButton(onClick = {
                        onConfirm()
                        onDismiss()
                    }) { Text("DONE") }
                }
            }
        }
    }
}


@Composable
fun DropdownMenuBox(selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Text(
            text = selected,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}