package com.example.todo.presentation.myDay

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todo.components.RepeatIntervalDialog
import com.example.todo.components.ShowDueDatePicker
import com.example.todo.presentation.myDay.components.AddTaskBottomSheet
import com.example.todo.presentation.myDay.components.ShowDateTimeDialog
import com.example.todo.presentation.myDay.components.TaskItem
import com.example.todo.presentation.update.scheduleReminderIfSet
import com.example.todo.ui.theme.lightGreen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDayScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    val viewModel: MyDayViewModel = hiltViewModel()
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())

    val showBottomSheet = remember { mutableStateOf(false) }
    val taskText = remember { mutableStateOf("") }

    val dueDateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val dueDate = remember { mutableLongStateOf(0L) }

    val reminderDate = remember { mutableLongStateOf(0L) }
    val reminderTime = remember { mutableLongStateOf(0L) }
    val timeDialogVisibility = remember { mutableStateOf(false) }

    val dateTimeDialogVisibility = remember { mutableStateOf(false) }


    val repeatOption = remember { mutableStateOf<String?>(null) }

    val repeatDialogVisibility = remember { mutableStateOf(false) }
    val repeatInterval = remember { mutableIntStateOf(1) }
    val repeatUnit = remember { mutableStateOf("weeks") }
    val selectedDays = remember { mutableStateListOf<String>() }

    val daysOfWeek = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")


    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Wallpaper background
        Image(
            painter = painterResource(id = R.drawable.wallpaper), // your image in res/drawable
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔹 Foreground content layered on top
        Column(modifier = Modifier.fillMaxSize()) {

            // 🔹 TopBar
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
            )

//            Spacer(modifier = Modifier.height(20.dp))
            // 🔹 Title
            Text(
                text = "My Day",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            // 🔹 Current Date
            Text(
                text = remember {
                    SimpleDateFormat(
                        "EEEEEE, d MMM",
                        Locale.getDefault()
                    ).format(Date())
                },
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 24.sp,
                color = Color.White
            )

            //list of tasks
            LazyColumn(modifier = Modifier.padding(4.dp)) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { updatedTask ->
                            viewModel.toggleComplete(updatedTask)
                        },
                        onToggleImportant = { updatedTask ->
                            viewModel.toggleImportant(updatedTask)
                        },
                        onClick = { selectedTask ->
                            navController.navigate("update/${task.id}")
                        }
                    )
                }

            }

        }

        // 🔹 FAB
        FloatingActionButton(
            onClick = { showBottomSheet.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .navigationBarsPadding(),
            containerColor = lightGreen,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
                tint = MaterialTheme.colorScheme.background
            )
        }

        AddTaskBottomSheet(
            showBottomSheet = showBottomSheet,
            taskText = taskText,
            dueDate = dueDate,
            reminderTime = reminderTime,
            reminderDate = reminderDate,
            selectedDays = selectedDays,
            repeatInterval = repeatInterval,
            repeatUnit = repeatUnit,
            viewModel = viewModel,
            dueDateDialogVisibility = dueDateDialogVisibility,
            dateTimeDialogVisibility = dateTimeDialogVisibility,
            repeatDialogVisibility = repeatDialogVisibility,
            scrollState = scrollState,
            onTaskAdded = { task, context ->
                // Merge reminder date + time into one timestamp
                scheduleReminderIfSet(task, context)
            }

        )


        if (dueDateDialogVisibility.value) {
            ShowDueDatePicker(
                onDateSelected = {
                    dueDate.longValue = it
                    dueDateDialogVisibility.value = false
                }, onDismiss = {
                    dueDateDialogVisibility.value = false
                }
            )
        }

        if (dateTimeDialogVisibility.value) {
            ShowDateTimeDialog(
                onDismiss = { dateTimeDialogVisibility.value = false },
                onDateSelected = { millis ->
                    reminderDate.longValue = millis

                },
                onTimeSelected = { millis ->
                    reminderTime.longValue = millis
                    dateTimeDialogVisibility.value = false
                }
            )
        }


        if (repeatDialogVisibility.value) {
            RepeatIntervalDialog(
                repeatInterval = repeatInterval.intValue,
                onRepeatIntervalChange = { repeatInterval.intValue = it },
                repeatUnit = repeatUnit.value,
                onRepeatUnitChange = { repeatUnit.value = it },
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
                    // Confirm logic here — maybe temporarily print to log:
                    Log.d("Repeat", "Interval: $repeatInterval $repeatUnit on ${selectedDays.joinToString()}")
                }
            )
        }


    }
}


@Composable
fun DayToggle(day: String, selected: Boolean, onToggle: () -> Unit) {
    val background = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray
    val contentColor = if (selected) Color.White else Color.Black
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(background)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Text(day, color = contentColor, style = MaterialTheme.typography.labelLarge)
    }
}