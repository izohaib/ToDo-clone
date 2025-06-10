package com.example.todo.presentation.myDay.components

import android.content.Context
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.core.Utils.Utils
import com.example.todo.data.local.Task
import com.example.todo.presentation.myDay.MyDayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    taskText: MutableState<String>,
    dueDate: MutableLongState,
    reminderTime: MutableLongState,
    reminderDate: MutableLongState,
    selectedDays: SnapshotStateList<String>,
    repeatInterval: MutableState<Int>,
    repeatUnit: MutableState<String>,
    viewModel: MyDayViewModel,
    dueDateDialogVisibility: MutableState<Boolean>,
    dateTimeDialogVisibility: MutableState<Boolean>,
    repeatDialogVisibility: MutableState<Boolean>,
    scrollState: ScrollState,
    onTaskAdded: (Task, Context) -> Unit //for reminder
) {
    // Remember the ModalBottomSheetState
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest    = { showBottomSheet.value = false },
            sheetState          = bottomSheetState,
            tonalElevation      = 0.dp,
            dragHandle          = null,
            // respect both system bars and the keyboard IME
            contentWindowInsets = { WindowInsets.systemBars.union(WindowInsets.ime) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    // also pad for navigation bars
                    .windowInsetsPadding(
                        WindowInsets.navigationBars
                            .union(WindowInsets.ime)
                    )
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment   = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { showBottomSheet.value = false },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(imageVector = Icons.Outlined.Circle, contentDescription = null)
                    }

                    OutlinedTextField(
                        value = taskText.value,
                        onValueChange = { taskText.value = it },
                        label = {
                            Text("Add a task", color = MaterialTheme.colorScheme.onSurface)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                    )
                    IconButton(
                        onClick = {
                            val task = Task(
                                title = taskText.value.trim(),
                                dueDate = if (dueDate.longValue != 0L) dueDate.longValue else null,
                                reminderTime = if (reminderTime.longValue != 0L ) reminderTime.longValue else null,
                                reminderDate = if (reminderDate.longValue != 0L) reminderDate.longValue else null,
                                repeatInterval = if (selectedDays.isNotEmpty()) repeatInterval.value else null,
                                repeatUnit = if (selectedDays.isNotEmpty()) repeatUnit.value else null,
                                repeatDays = if (selectedDays.isNotEmpty()) selectedDays.toList() else null,
                                createdAt = System.currentTimeMillis(),
                                isCompleted = false,
                                isImportant = false,
                            )
//
                            if (task.title.isNotEmpty()) {

                                // Only call addTaskWithId
                                viewModel.addTaskWithId(task) { insertedTask ->
                                    Log.d("AddTaskBottomSheet", "Task inserted successfully with ID: ${insertedTask.id}")

                                    // This callback runs after successful insertion with the real ID
                                    if (insertedTask.reminderTime != null && insertedTask.reminderDate != null) {
                                        Log.d("AddTaskBottomSheet", "Setting up notification for task ID: ${insertedTask.id}")
                                        onTaskAdded(insertedTask, context)
                                    }
                                }
                            }

                            // Reset the UI
                            taskText.value = ""
                            dueDate.longValue = 0L
                            reminderTime.longValue = 0L
                            reminderDate.longValue = 0L
                            selectedDays.clear()
                            repeatInterval.value = 1
                            repeatUnit.value = "weeks"
                            showBottomSheet.value = false
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = null)
                    }
                }

                Spacer(Modifier.height(2.dp))

                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    //date dialog
                    Surface (
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                dueDateDialogVisibility.value = true
                            }
                            .background(
                                if (dueDate.longValue != 0L)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        tonalElevation = 2.dp,
                        color = Color.Transparent // ← ADD THIS to make Surface transparent
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Due Date",
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            val formattedDate = if (dueDate.longValue != 0L) {
                                Utils.convertLongToDate(dueDate.longValue)
                            } else {
                                "Set Due Date"
                            }
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (dueDate.longValue != 0L) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Date",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            dueDate.longValue = 0L // Reset to 0 (empty state)
                                        }
                                )
                            }
                        }
                    }


                    //date & time dialog
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                dateTimeDialogVisibility.value = true
                            }
                            .background(
                                if (reminderTime.longValue != 0L)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )

                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        tonalElevation = 2.dp,
                        color = Color.Transparent
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Remind Me",
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            val reminderText = if (reminderTime.longValue != 0L && reminderDate.longValue != 0L) {
                                "${Utils.convertLongToDate(reminderDate.longValue)} at" +
                                        " ${Utils.convertLongToTime(reminderTime.longValue)}"
                            } else {
                                "Set Reminder"
                            }

                            Text(
                                text = reminderText,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (reminderTime.longValue != 0L && reminderDate.longValue != 0L) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Reminder",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            reminderTime.longValue = 0L
                                            reminderDate.longValue = 0L
                                        }
                                )
                            }
                        }
                    }

                    //select days
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                repeatDialogVisibility.value = true
                            }
                            .background(
                                if (selectedDays.isNotEmpty())
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        tonalElevation = 2.dp,
                        color = Color.Transparent
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repeat",
                                modifier = Modifier.size(18.dp),
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            val text = if (selectedDays.isEmpty()) {
                                "Repeat"
                            } else {
                                //"Weekly $repeatInterval $repeatUnit on ${selectedDays.joinToString()}"
                                val previewDays = selectedDays.take(3) // take first 3 days
                                val preview = previewDays.joinToString()
                                val suffix = if (selectedDays.size > 3) "…" else ""
                                "Weekly $repeatInterval $repeatUnit on $preview$suffix"
                            }

                            Text(
                                text = text ,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (selectedDays.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Repeat",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            // Clear the repeat selection
                                            repeatInterval.value = 1
                                            repeatUnit.value = "Day"
                                            selectedDays.clear()
                                        },
                                    tint = Color.Black,
                                )
                            }

                        }
                    }
                }

            }
        }
    }
}