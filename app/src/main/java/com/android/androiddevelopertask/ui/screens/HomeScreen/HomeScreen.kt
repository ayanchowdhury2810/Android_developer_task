package com.android.androiddevelopertask.ui.screens.HomeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
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
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var newNoteText by remember { mutableStateOf("") }

//    val todos by viewModel.todos.collectAsState()
    val todoResponse: Resource<TodoResult>? by viewModel.todoResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getTodoData()
    }

    when(todoResponse?.status){
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

    Scaffold {innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(White)
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    TextField(
//                        value = newNoteText,
//                        onValueChange = { newNoteText = it },
//                        modifier = Modifier.weight(1f),
//                        label = { Text("New Note") }
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Button(onClick = {
//                        if (newNoteText.isNotBlank()) {
//                            viewModel.addNote(newNoteText)
//                            newNoteText = ""
//                        }
//                    }) {
//                        Text("Add")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                LazyColumn {
//                    items(todos) { note ->
//                        NoteItem(note = note, onDelete = {
////                            viewModel.removeNote(it)
//                        })
//                    }
//                }
//            }
            if(todoResponse?.data != null){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    item{
                        Text(text = "FROM API", style = TextStyle(color = Black))
                    }
                    items(todoResponse?.data!!){it->
                        if(!it?.title.isNullOrEmpty()) {
                            Text(text = it?.title.toString(), style = TextStyle(color = Black))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: TodoResultItem, onDelete: (TodoResultItem) -> Unit) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(note.title.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onDelete(note) }) {
                Text("Delete")
            }
        }
    }
}