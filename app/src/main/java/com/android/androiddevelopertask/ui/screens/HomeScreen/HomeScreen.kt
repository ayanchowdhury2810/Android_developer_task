package com.android.androiddevelopertask.ui.screens.HomeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.androiddevelopertask.dataClass.TodoResult
import com.android.androiddevelopertask.dataClass.TodoResultItem
import com.android.androiddevelopertask.retrofit.Resource
import com.android.androiddevelopertask.retrofit.Status

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit
) {


    val todos by viewModel.todos.collectAsState()
    val todoResponse: Resource<TodoResult>? by viewModel.todoResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getTodoData()
    }

    when (todoResponse?.status) {
        Status.SUCCESS -> {
            Log.d("HomeScreen", "result-> ${todoResponse?.data}")
        }

        Status.ERROR -> {
            Log.d("HomeScreen", "Something went wrong")

        }

        Status.LOADING -> {
            Log.d("HomeScreen", "Loading")
        }

        null -> {}
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(White)
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Button(onClick = {
                onAddClick()
            }) {
                Text("Add")
            }
            if (todoResponse?.data != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp)
                ) {

                    items(todos) { note ->
                        NoteItem(note = note, onDelete = {
                            viewModel.deleteNote(it)
                        },
                            onEdit = {
                                viewModel.getEditData(it)
                                onEditClick()
                            })
                    }

                    item {
                        Text(text = "FROM API", style = TextStyle(color = Black))
                    }
                    todoResponse?.data?.let { data ->
                        items(data) { it ->
                            it?.let { it1 ->
                                NoteItem(it1, shouldDisplayButtons = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: TodoResultItem,
    shouldDisplayButtons: Boolean = true,
    onDelete: (TodoResultItem) -> Unit = {},
    onEdit: (TodoResultItem) -> Unit = {}
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(note.title.toString())
            Spacer(modifier = Modifier.width(8.dp))
            if (shouldDisplayButtons) {
                Button(onClick = { onDelete(note) }) {
                    Text("Delete")
                }

                Button(onClick = { onEdit(note) }) {
                    Text("Edit")
                }
            }
        }
    }
}