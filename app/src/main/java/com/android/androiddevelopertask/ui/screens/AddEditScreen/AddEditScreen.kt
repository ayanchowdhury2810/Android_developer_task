package com.android.androiddevelopertask.ui.screens.AddEditScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import com.android.androiddevelopertask.ui.screens.HomeScreen.HomeScreenViewModel

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    backPressed: () -> Unit
) {

    val editData by viewModel.editData.observeAsState()

    var newNoteText by remember { mutableStateOf("") }

    if(editData != null) {
        LaunchedEffect(Unit) {
            newNoteText = editData?.title.toString()
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
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if(editData==null) {
                        if (newNoteText.isNotBlank()) {
                            viewModel.addNote(newNoteText)
                            newNoteText = ""
                        }
                    } else {
                        editData?.let {
                            if (newNoteText.isNotBlank()) {
                                viewModel.updateNote(newNoteText, it.id)
                                newNoteText = ""
                            }
                        }
                    }
                    backPressed()
                }) {
                    if(editData==null) {
                        Text("Add")
                    }else {
                        Text("Edit")
                    }
            }

        }
    }
}