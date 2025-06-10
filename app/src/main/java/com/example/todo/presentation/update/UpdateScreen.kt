package com.example.todo.presentation.update

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.todo.components.RepeatIntervalDialog
import com.example.todo.components.ShowDueDatePicker
import com.example.todo.presentation.myDay.MyDayViewModel
import com.example.todo.presentation.myDay.components.ShowDateTimeDialog
import com.example.todo.presentation.update.components.AddStepRow
import com.example.todo.presentation.update.components.DueDatePickerRow
import com.example.todo.presentation.update.components.FilePickerSection
import com.example.todo.presentation.update.components.MyDayToggle
import com.example.todo.presentation.update.components.NoteSection
import com.example.todo.presentation.update.components.ReminderPickerRow
import com.example.todo.presentation.update.components.RepeatPickerRow
import com.example.todo.ui.theme.lightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    taskId: Int,
    onBack: () -> Unit
) {
    val viewModel: MyDayViewModel = hiltViewModel()
    val context = LocalContext.current

    val allTasks by viewModel.allTasks.collectAsState()
    // Find the task by ID from the collected list
    val task = allTasks.find { it.id == taskId }
    if (task == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading task...")
        }
        return
    }

    // Convert task.fileUris (List<String>) to List<Uri> for UI
    var fileUris by remember {
        mutableStateOf(task.fileUri.map { Uri.parse(it) })
    }

    var note by remember { mutableStateOf(task.note ?: "") }
    var title by remember { mutableStateOf(task.title) }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }
    var isImportant by remember { mutableStateOf(task.isImportant) }
    var isMyDay by remember { mutableStateOf(task.isMyDay) }
    var remindTime by remember { mutableLongStateOf(task.reminderTime ?: 0L) }
    var remindDate by remember { mutableLongStateOf(task.reminderDate ?: 0L) }
    val dueDate = remember { mutableLongStateOf(task.dueDate ?: 0L) }

    var steps by remember { mutableStateOf(task.steps) }
    var newStep by remember { mutableStateOf("") }
    val dateTimeDialogVisibility = remember { mutableStateOf(false) }
    val dueDateDialogVisibility = remember { mutableStateOf(false) }

    val repeatDialogVisibility = remember { mutableStateOf(false) }
    var repeatInterval by remember { mutableIntStateOf(1) }
    var repeatUnit by remember { mutableStateOf("weeks") }
    val selectedDays = remember { mutableStateListOf<String>() }
    var previousReminderTime by remember { mutableLongStateOf(task.reminderTime ?: 0L) }
    var previousReminderDate by remember { mutableLongStateOf(task.reminderDate ?: 0L) }


    fun triggerUpdate() {
        val updatedTask = task.copy(
            title = title,
            isCompleted = isCompleted,
            isImportant = isImportant,
            isMyDay = isMyDay,
            reminderTime = remindTime,
            reminderDate = remindDate,
            steps = steps,
            dueDate = dueDate.longValue.takeIf { it != 0L },
            fileUri = fileUris.map { it.toString() },
            note = note
        )
        viewModel.updateTask(updatedTask)
        if (remindTime != previousReminderTime || remindDate != previousReminderDate) {
            scheduleReminderIfSet(updatedTask, context)
            previousReminderTime = remindTime
            previousReminderDate = remindDate
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Task",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
            )
        ) {
            // Task title and completion row
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        isCompleted = !isCompleted
                        triggerUpdate()
                    }) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Toggle Complete",
                            tint =  lightGreen,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            triggerUpdate()
                        },
                        placeholder = { Text("Enter task title") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )

                    IconToggleButton(
                        checked = isImportant,
                        onCheckedChange = {
                            isImportant = it
                            triggerUpdate()
                        }
                    ) {
                        Icon(
                            imageVector = if (isImportant) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Toggle Important",
                            tint = lightGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Steps section
            item {
                AddStepRow(
                    steps = steps,
                    onStepsChanged = { newSteps ->
                        steps = newSteps
                        triggerUpdate()
                    },
                    triggerUpdate = {
                        triggerUpdate()
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Add to My Day toggle
            item {
                MyDayToggle(
                    isMyDay = isMyDay,
                    onToggle = {
                        isMyDay = it
                        triggerUpdate()
                    }
                )
            }

            // Reminder section
            item {
                ReminderPickerRow(
                    remindDate = remindDate,
                    remindTime = remindTime,
                    onOpenDateTimePicker = {
                        dateTimeDialogVisibility.value = true
                    },
                    onClearReminder = {
                        remindDate = 0L
                        remindTime = 0L
                        triggerUpdate()
                    },
                )
            }

            // DueDate section
            item {
                DueDatePickerRow(
                    dueDate = dueDate.longValue,
                    onOpenDatePicker = { dueDateDialogVisibility.value = true },
                    onClearDate = {
                        dueDate.longValue = 0L
                        triggerUpdate()
                    }
                )
            }

            // Repeat section
            item {
                RepeatPickerRow(
                    repeatInterval = repeatInterval,
                    repeatUnit = repeatUnit,
                    selectedDays = selectedDays,
                    onOpenRepeatDialog = {
                        repeatDialogVisibility.value = true
                        triggerUpdate()
                    },
                    onClearRepeat = {
                        repeatInterval = 1
                        repeatUnit = "Day"
                        selectedDays.clear()
                        triggerUpdate()
                    }
                )
            }

            // File picker section
            item {
                FilePickerSection(
                    fileUris = fileUris,
                    onAddFile = { uri ->
                        fileUris = fileUris + uri
                        triggerUpdate()
                    },
                    onRemoveFile = { uri ->
                        fileUris = fileUris - uri
                        triggerUpdate()
                    }
                )
            }

            // Note section - This is the problematic one with keyboard
            item {
                NoteSection(
                    note = note,
                    onNoteChange = { newNote ->
                        note = newNote
                        triggerUpdate()
                    }
                )
            }
        }

        // All your dialogs remain the same
        if (dateTimeDialogVisibility.value) {
            ShowDateTimeDialog(
                onDismiss = { dateTimeDialogVisibility.value = false },
                onDateSelected = { millis ->
                    remindDate = millis
                },
                onTimeSelected = { millis ->
                    remindTime = millis
                    triggerUpdate()
                    dateTimeDialogVisibility.value = false
                }
            )
        }

        if (dueDateDialogVisibility.value) {
            ShowDueDatePicker(
                onDateSelected = {
                    dueDate.longValue = it
                    triggerUpdate()
                    dueDateDialogVisibility.value = false
                }, onDismiss = {
                    dueDateDialogVisibility.value = false
                }
            )
        }

        if (repeatDialogVisibility.value) {
            RepeatIntervalDialog(
                repeatInterval = repeatInterval,
                onRepeatIntervalChange = { repeatInterval = it },
                repeatUnit = repeatUnit,
                onRepeatUnitChange = { repeatUnit = it },
                selectedDays = selectedDays,
                onToggleDay = { day ->
                    if (selectedDays.contains(day)) {
                        selectedDays.remove(day)
                    } else {
                        selectedDays.add(day)
                    }
                },
                onDismiss = { repeatDialogVisibility.value = false },
                onConfirm = {
                    Log.d("Repeat", "Interval: $repeatInterval $repeatUnit on ${selectedDays.joinToString()}")
                }
            )
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UpdateScreen(
//    taskId: Int,
//    onBack: () -> Unit
//) {
//    val viewModel: MyDayViewModel = hiltViewModel()
//    val context = LocalContext.current
//
//    val allTasks by viewModel.allTasks.collectAsState()
//    // Find the task by ID from the collected list
//    val task = allTasks.find { it.id == taskId }
//    if (task == null) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Loading task...")
//        }
//        return
//    }
//
//// Convert task.fileUris (List<String>) to List<Uri> for UI
//    var fileUris by remember {
//        mutableStateOf(task.fileUri.map { Uri.parse(it) })
//    }
//
//    var note by remember { mutableStateOf(task.note ?: "") }
//
//
//    var title by remember { mutableStateOf(task.title) }
//    var isCompleted by remember { mutableStateOf(task.isCompleted) }
//    var isImportant by remember { mutableStateOf(task.isImportant) }
//    var isMyDay by remember { mutableStateOf(task.isMyDay) }
//    var remindTime by remember { mutableLongStateOf(task.reminderTime ?: 0L) }
//    var remindDate by remember { mutableLongStateOf(task.reminderDate ?: 0L) }
//    val dueDate = remember { mutableLongStateOf(task.dueDate ?: 0L) }
//
//    var steps by remember { mutableStateOf(task.steps) }
//    var newStep by remember { mutableStateOf("") }
//    val dateTimeDialogVisibility = remember { mutableStateOf(false) }
//    val dueDateDialogVisibility = remember { mutableStateOf(false) }
//
//    val repeatDialogVisibility = remember { mutableStateOf(false) }
//    var repeatInterval by remember { mutableIntStateOf(1) }
//    var repeatUnit by remember { mutableStateOf("weeks") }
//    val selectedDays = remember { mutableStateListOf<String>() }
//
//
//
//
//    fun triggerUpdate() {
//        val updatedTask = task.copy(
//            title = title,
//            isCompleted = isCompleted,
//            isImportant = isImportant,
//            isMyDay = isMyDay,
//            reminderTime = remindTime,
//            reminderDate = remindDate,
//            steps = steps,
//            dueDate = dueDate.longValue.takeIf { it != 0L },
//            fileUri = fileUris.map { it.toString() },
//            note = note
//        )
//        viewModel.updateTask(updatedTask)
//        scheduleReminderIfSet(updatedTask,context)
//    }
//
////    val scrollState = rememberScrollState()
////    val bringIntoViewRequester = remember { BringIntoViewRequester() }
////    val nestedScrollConnection = remember {
////        object : NestedScrollConnection {}
////    }
//
//
//    val scrollState = rememberScrollState()
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Task") },
//                navigationIcon = {
//                    IconButton(onClick = { onBack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Column(
////            modifier = Modifier
////                .padding(padding)
////                .padding(16.dp)
////                .fillMaxSize()
////                .verticalScroll(rememberScrollState())
////                .imePadding()
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                // This handles both navigation bars and IME padding
//                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
//                .padding(16.dp)
//        ) {
//
//            // Task title and completion row
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                IconButton(onClick = {
//                    isCompleted = !isCompleted
//                    triggerUpdate()
//                }) {
//                    Icon(
//                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
//                        contentDescription = "Toggle Complete",
//                        tint = if (isCompleted) Color.Green else Color.Gray
//                    )
//                }
//
//                TextField(
//                    value = title,
//                    onValueChange = {
//                        title = it
//                        triggerUpdate()
//                    },
//                    placeholder = { Text("Enter task title") },
//                    modifier = Modifier.weight(1f),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent
//                    )
//
//                )
//
//                IconToggleButton(
//                    checked = isImportant,
//                    onCheckedChange = {
//                        isImportant = it
//                        triggerUpdate()
//                    }
//                ) {
//                    Icon(
//                        imageVector = if (isImportant) Icons.Default.Star else Icons.Default.StarBorder,
//                        contentDescription = "Toggle Important",
//                        tint = if (isImportant) Color.Yellow else Color.Gray
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            //steps
//           AddStepRow(
//               steps = steps,
//               onStepsChanged = { newSteps ->
//                   steps = newSteps
//                   triggerUpdate()
//               },
//               triggerUpdate = {
//                   triggerUpdate()
//               },
//           )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Add to My Day toggle
//           MyDayToggle(
//               isMyDay = isMyDay,
//               onToggle = {
//                   isMyDay = it
//                   triggerUpdate()
//               }
//           )
//
////            Spacer(modifier = Modifier.height(16.dp))
//
//            // Reminder section
//            ReminderPickerRow(
//                remindDate = remindDate,
//                remindTime = remindTime,
//                onOpenDateTimePicker = {
//                    dateTimeDialogVisibility.value = true
//                },
//                onClearReminder = {
//                    remindDate = 0L
//                    remindTime = 0L
//                    triggerUpdate()
//                },
//            )
//            if (dateTimeDialogVisibility.value) {
//                ShowDateTimeDialog(
//                    onDismiss = { dateTimeDialogVisibility.value = false },
//                    onDateSelected = { millis ->
//                        remindDate = millis
//                    },
//                    onTimeSelected = { millis ->
//                        remindTime = millis
//                        triggerUpdate()
//                        dateTimeDialogVisibility.value = false
//                    }
//                )
//            }
//
//            // DueDate section
//            DueDatePickerRow(
//                dueDate = dueDate.longValue,
//                onOpenDatePicker = { dueDateDialogVisibility.value = true },
//                onClearDate = {
//                    dueDate.longValue = 0L
//                    triggerUpdate()
//                }
//            )
//
//
//            //Repeat section
//            RepeatPickerRow (
//                repeatInterval = repeatInterval,
//                repeatUnit = repeatUnit,
//                selectedDays = selectedDays,
//                onOpenRepeatDialog = {
//                    repeatDialogVisibility.value = true
//                    triggerUpdate()
//                },
//                onClearRepeat = {
//                    repeatInterval = 1
//                    repeatUnit = "Day"
//                    selectedDays.clear()
//                    triggerUpdate()
//                }
//            )
//
//
//            //Add  file
//            FilePickerSection(
//                fileUris = fileUris,
//                onAddFile = { uri ->
//                    fileUris = fileUris + uri
//                    triggerUpdate()
//                },
//                onRemoveFile = { uri ->
//                    fileUris = fileUris - uri
//                    triggerUpdate()
//                }
//            )
//
//            NoteSection(
//                note = note,
//                onNoteChange = { newNote ->
//                    note = newNote
//                    triggerUpdate()
//                },
////                parentBringIntoViewRequester = bringIntoViewRequester
//            )
//
//        }
//
//        if (dateTimeDialogVisibility.value) {
//            ShowDateTimeDialog(
//                onDismiss = { dateTimeDialogVisibility.value = false },
//                onDateSelected = { millis ->
//                    remindDate = millis
//                },
//                onTimeSelected = { millis ->
//                    remindTime = millis
//                    triggerUpdate()
//                    dateTimeDialogVisibility.value = false
//                }
//            )
//        }
//
//        if (dueDateDialogVisibility.value) {
//            ShowDueDatePicker(
//                onDateSelected = {
//                    dueDate.longValue = it
//                    triggerUpdate()
//                    dueDateDialogVisibility.value = false
//                }, onDismiss = {
//                    dueDateDialogVisibility.value = false
//                }
//            )
//        }
//        if (repeatDialogVisibility.value) {
//            RepeatIntervalDialog(
//                repeatInterval = repeatInterval,
//                onRepeatIntervalChange = { repeatInterval = it },
//                repeatUnit = repeatUnit,
//                onRepeatUnitChange = { repeatUnit = it },
//                selectedDays = selectedDays,
//                onToggleDay = { day ->
//                    if (selectedDays.contains(day)) {
//                        selectedDays.remove(day)
//                    } else {
//                        selectedDays.add(day)
//                    }
//                },
//                onDismiss = { repeatDialogVisibility.value = false },
//                onConfirm = {
//                    // Confirm logic here — maybe temporarily print to log:
//                    Log.d("Repeat", "Interval: $repeatInterval $repeatUnit on ${selectedDays.joinToString()}")
//                }
//            )
//        }
//    }
//}

