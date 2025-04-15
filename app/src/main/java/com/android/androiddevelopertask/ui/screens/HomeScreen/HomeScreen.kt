package com.android.androiddevelopertask.ui.screens.HomeScreen

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.androiddevelopertask.R
import com.android.androiddevelopertask.dataClass.TodoResult
import com.android.androiddevelopertask.dataClass.TodoResultItem
import com.android.androiddevelopertask.retrofit.Resource
import com.android.androiddevelopertask.retrofit.Status
import java.util.Locale
import java.util.concurrent.TimeUnit

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
            Button(
                modifier= Modifier
                    .padding(30.dp)
                    .fillMaxWidth(),
                onClick = {
                onAddClick()
            }) {
                Text("Add")
            }
            if (todoResponse?.data != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, end = 10.dp, start = 10.dp)
                ) {
                    if(!todos.isNullOrEmpty()) {
                        item {
                            Text(text = "Your Reminders", style = TextStyle(color = Black, fontSize = 30.sp))
                        }
                    }

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
                        Text(text = "Reminders from server", style = TextStyle(color = Black, fontSize = 30.sp), modifier = Modifier.padding(vertical = 10.dp))
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
    var isSpeaking by remember { mutableStateOf(false) }
    val tts = rememberTextToSpeech()

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!note.title.isNullOrEmpty()) {
                Row{
                    Text(
                        note.title.toString(),
                        style = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.ic_sound),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (tts.value?.isSpeaking == true) {
                                    tts.value?.stop()
                                    isSpeaking = false
                                } else {
                                    tts.value?.speak(
                                        note.title, TextToSpeech.QUEUE_FLUSH, null, ""
                                    )
                                    isSpeaking = true
                                }
                            },
                        colorFilter = ColorFilter.tint(White)
                    )
                }

            }

           if(!note.description.isNullOrEmpty()) {
               Row(modifier = Modifier.padding(top = 5.dp)){
                   Text(
                       note.description.toString() ,
                       style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                       overflow = TextOverflow.Ellipsis,
                       maxLines = 1
                   )
                   Spacer(modifier = Modifier.weight(1f))
                   Image(
                       painter = painterResource(R.drawable.ic_sound),
                       contentDescription = null,
                       modifier = Modifier
                           .size(20.dp)
                           .clickable {
                               if (tts.value?.isSpeaking == true) {
                                   tts.value?.stop()
                                   isSpeaking = false
                               } else {
                                   tts.value?.speak(
                                       note.description, TextToSpeech.QUEUE_FLUSH, null, ""
                                   )
                                   isSpeaking = true
                               }
                           },
                       colorFilter = ColorFilter.tint(White)
                   )
               }
           }
            if(note.startTimeMillis!=null){
                val (hours, minutes) = millisecondsToHoursAndMinutes(note.startTimeMillis)
                Text(
                    "Start Time -> $hours: $minutes",
                    style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            if(note.recurrenceIntervalMillis!=null){
                val hrs = millisecondsToHours(note.recurrenceIntervalMillis)
                Text(
                    "Repeat in every - $hrs hrs",
                    style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
}

@Composable
fun rememberTextToSpeech(): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.US
            }
        }
        tts.value = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}

fun millisecondsToHoursAndMinutes(milliseconds: Long): Pair<Int, Int> {
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
    return Pair(hours.toInt(), minutes.toInt())
}

fun millisecondsToHours(milliseconds: Long): Int {
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    return hours.toInt()
}