package com.example.todo.presentation.myDay.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.data.local.Task
import com.example.todo.ui.theme.lightGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: (Task) -> Unit,
    onToggleImportant: (Task) -> Unit,
    onClick: (Task) -> Unit,
//    onLongClick: (Task) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = { onClick(task) }
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onToggleComplete(task) }) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Complete",
                tint = lightGreen
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, color = lightGreen)
            task.dueDate?.let {
                val dateText = SimpleDateFormat("d MMM, yyyy", Locale.getDefault()).format(Date(it))
                Text(text = dateText, fontSize = 12.sp, color = Color.LightGray)
            }
        }

        IconButton(onClick = { onToggleImportant(task) }) {
            Icon(
                imageVector = if (task.isImportant) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Important",
                tint =  lightGreen
            )
        }
    }
}