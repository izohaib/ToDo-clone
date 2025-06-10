package com.example.todo.presentation.myDay.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDateTimeDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (Long) -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val tabTitles = listOf("DATE", "TIME")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                // Modern, compact TabRow
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    title,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            modifier = Modifier.height(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (selectedTabIndex) {
                    0 -> {
                        // DATE tab
                        DatePicker(
                            state = datePickerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {

                                    if (datePickerState.selectedDateMillis != null) {
                                        onDateSelected(datePickerState.selectedDateMillis!!)
                                        selectedTabIndex = 1
                                    }
                                },
                                enabled = datePickerState.selectedDateMillis != null
                            ) {
                                Text("Next")
                            }
                        }
                    }

                    1 -> {
                        // TIME tab
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TimePicker(state = timePickerState)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    val dateMillis = datePickerState.selectedDateMillis ?: return@TextButton
                                    val calendar = Calendar.getInstance().apply {
                                        timeInMillis = dateMillis
                                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                        set(Calendar.MINUTE, timePickerState.minute)
                                        set(Calendar.SECOND, 0)
                                        set(Calendar.MILLISECOND, 0)
                                    }
                                    onTimeSelected(calendar.timeInMillis)
                                }
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDateTimeDialoga(
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Long) -> Unit
) {
    val showDatePicker = remember { mutableStateOf(true) }
    val datePickerState = rememberDatePickerState()
    val selectedDateInMillis = remember { mutableStateOf<Long?>(null) }
    val timePickerState = rememberTimePickerState()

    // Step 1: Show DatePickerDialog
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        selectedDateInMillis.value = selectedDate
                        onDateSelected(selectedDate)
                        showDatePicker.value = false
                    }
                }) {
                    Text("Next")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Step 2: Show TimePicker Dialog
    if (!showDatePicker.value) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .wrapContentHeight()
                    .widthIn(min = 280.dp, max = 360.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TimePicker(state = timePickerState)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            selectedDateInMillis.value?.let { dateMillis ->
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = dateMillis
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE, timePickerState.minute)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                onTimeSelected(calendar.timeInMillis)
                            }
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}
