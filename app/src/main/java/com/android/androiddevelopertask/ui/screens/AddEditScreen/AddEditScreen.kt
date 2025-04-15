package com.android.androiddevelopertask.ui.screens.AddEditScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreenViewModel
import com.android.androiddevelopertask.ui.screens.HomeScreen.millisecondsToHours
import com.android.androiddevelopertask.ui.screens.HomeScreen.millisecondsToHoursAndMinutes
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    backPressed: () -> Unit
) {

    val context = LocalContext.current

    val editData by viewModel.editData.observeAsState()

    var newNoteText by remember { mutableStateOf("") }
    var newNoteDescText by remember { mutableStateOf("") }
    var recurrentText by remember { mutableStateOf("") }

    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember {
        mutableStateOf("Pick time")
    }

    if (editData != null) {
        LaunchedEffect(Unit) {
            newNoteText = editData?.title.toString()
            newNoteDescText = editData?.description.toString()
            editData?.recurrenceIntervalMillis?.let {
                recurrentText = millisecondsToHours(it).toString()
            }
            editData?.startTimeMillis?.let {
                val (hours, minutes) = millisecondsToHoursAndMinutes(it)
                selectedTime = "$hours: $minutes "
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearEditData()
        }
    }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = newNoteText,
                onValueChange = { newNoteText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("New Note") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    showTimePicker = true
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = selectedTime)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newNoteDescText,
                onValueChange = { newNoteDescText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = recurrentText,
                onValueChange = {
                    recurrentText = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Repeating time in hours") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (editData == null) {
                    if (newNoteText.isNotBlank() && newNoteDescText.isNotBlank() && recurrentText.isNotBlank() && selectedTime != "Pick time") {
                        viewModel.addNote(
                            newNoteText, context, getTimeInMilliseconds(
                                hours = timePickerState.hour,
                                minutes = timePickerState.minute,
                            ),
                            description = newNoteDescText,
                            recurrenceTime = TimeUnit.HOURS.toMillis(
                                recurrentText.toInt().toLong()
                            )
                        )
                        newNoteText = ""
                        backPressed()
                    }
                } else {
                    editData?.let {
                        if (newNoteText.isNotBlank() && newNoteDescText.isNotBlank() && recurrentText.isNotBlank() && selectedTime != "Pick time") {
                            viewModel.updateNote(
                                newNoteText, it.id, getTimeInMilliseconds(
                                    hours = timePickerState.hour,
                                    minutes = timePickerState.minute
                                ),
                                description = newNoteDescText,
                                recurrenceTime = TimeUnit.HOURS.toMillis(
                                    recurrentText.toInt().toLong()
                                )
                            )
                            newNoteText = ""
                            backPressed()
                        }
                    }
                }
            }) {
                if (editData == null) {
                    Text("Add")
                } else {
                    Text("Edit")
                }
            }

        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                selectedTime = "${timePickerState.hour} : ${timePickerState.minute}"
                showTimePicker = false
            },
            dismissButton = { showTimePicker = false }
        )
        {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {

                    Text("Dismiss", modifier = Modifier.clickable {
                        dismissButton()
                    })
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Confirm", modifier = Modifier.clickable {
                        confirmButton()
                    })
                }
            }
        }
    }
}

fun getTimeInMilliseconds(hours: Int, minutes: Int): Long {
    return TimeUnit.HOURS.toMillis(hours.toLong()) +
            TimeUnit.MINUTES.toMillis(minutes.toLong())
}