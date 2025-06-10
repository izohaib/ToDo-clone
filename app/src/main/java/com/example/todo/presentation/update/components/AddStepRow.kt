package com.example.todo.presentation.update.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todo.data.local.Step

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStepRow(
    steps: List<Step>,
    onStepsChanged: (List<Step>) -> Unit,
    triggerUpdate: () -> Unit
) {
    var newStep by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var stepStates by remember { mutableStateOf(steps) }

    Column {
        stepStates.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = if (step.isCompleted) Icons.Default.Check else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            val updatedSteps = stepStates.toMutableList()
                            updatedSteps[index] = step.copy(isCompleted = !step.isCompleted)
                            stepStates = updatedSteps
                            onStepsChanged(updatedSteps)
                            triggerUpdate()
                        }
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = step.title,
                    onValueChange = { updatedText ->
                        val updatedSteps = stepStates.toMutableList()
                        updatedSteps[index] = step.copy(title = updatedText)
                        stepStates = updatedSteps
                        onStepsChanged(updatedSteps)
                        triggerUpdate()
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )

                )
            }
        }
    }

    // Add new step row
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isTyping = true
                focusRequester.requestFocus()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = if (isTyping) Icons.Default.RadioButtonUnchecked else Icons.Default.Add,
            contentDescription = "Step Icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        TextField(
            value = newStep,
            onValueChange = { newStep = it },
            placeholder = { Text("Add step") },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && newStep.isNotBlank()) {
                        val updatedSteps = stepStates + Step(title = newStep, isCompleted = false)
                        stepStates = updatedSteps
                        onStepsChanged(updatedSteps)
                        newStep = ""
                        isTyping = false
                        triggerUpdate()
                    }
                },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
            ,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (newStep.isNotBlank()) {
                    val updatedSteps = stepStates + Step(title = newStep)
                    stepStates = updatedSteps
                    onStepsChanged(updatedSteps)
                    newStep = ""
                    isTyping = false
                    focusManager.clearFocus()
                    triggerUpdate()
                }
            })
        )
    }
}