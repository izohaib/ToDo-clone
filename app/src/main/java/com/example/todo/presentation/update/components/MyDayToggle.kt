package com.example.todo.presentation.update.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyDayToggle(
    isMyDay: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp)
            .clickable {
                onToggle(!isMyDay) // Toggle value on click
            },

    ) {
        Icon(Icons.Default.WbSunny, contentDescription = null)
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = if (isMyDay) "Added to My Day" else "Add to My Day",
            modifier = Modifier.weight(1f)
        )
        if (isMyDay) {
            IconButton(
                onClick = { onToggle(false) },
            ) {
                Icon(Icons.Outlined.Close, contentDescription = "Remove from My Day")
            }

        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}