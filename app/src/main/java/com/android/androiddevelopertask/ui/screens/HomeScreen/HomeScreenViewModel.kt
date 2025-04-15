package com.android.androiddevelopertask.ui.screens.HomeScreen

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.android.androiddevelopertask.dataClass.TodoResult
import com.android.androiddevelopertask.dataClass.TodoResultItem
import com.android.androiddevelopertask.retrofit.Resource
import com.android.androiddevelopertask.ui.Repository
import com.android.androiddevelopertask.utilities.TaskNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val resources: Resources
) : ViewModel() {

    private var _todoResult = MutableLiveData<Resource<TodoResult>>()
    val todoResult: LiveData<Resource<TodoResult>> = _todoResult

    private var _editData = MutableLiveData<TodoResultItem?>()
    val editData: LiveData<TodoResultItem?> = _editData

    val todos = repository.allTodos.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun getTodoData() {
        viewModelScope.launch(Dispatchers.IO) {
            _todoResult.postValue(Resource.loading(null))
            val response = repository.getTodos()
            if (response != null) {
                _todoResult.postValue(Resource.success(response))
            } else {
                _todoResult.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun getEditData(note: TodoResultItem) {
        _editData.value = note
    }

    fun clearEditData() {
        _editData.value = null
    }

    fun addNote(content: String, context: Context, startTimeMillis: Long, description: String, recurrenceTime: Long) {
        viewModelScope.launch {
            scheduleTaskIfNeeded(
                TodoResultItem(title = content, completed = false, userId = null, startTimeMillis = startTimeMillis, description = description, recurrenceIntervalMillis = recurrenceTime),
                context
            )
            repository.insert(TodoResultItem(title = content, completed = false, userId = null, startTimeMillis = startTimeMillis, description = description, recurrenceIntervalMillis = recurrenceTime))

        }
    }

    fun deleteNote(todo: TodoResultItem) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    fun updateNote(title: String, id: Int, startTimeMillis: Long, description: String, recurrenceTime: Long) {
        viewModelScope.launch {
            repository.update(
                TodoResultItem(
                    title = title,
                    completed = false,
                    userId = null,
                    id = id,
                    startTimeMillis = startTimeMillis,
                    description = description,
                    recurrenceIntervalMillis = recurrenceTime
                )
            )
        }
    }

    private fun scheduleTaskIfNeeded(task: TodoResultItem, context: Context) {
        val workManager = WorkManager.getInstance(context)

        Log.d("AAAAAAA", "555555")

        workManager.getWorkInfosForUniqueWorkLiveData("TaskNotification_${task.id}")
            .observeForever { workInfos ->
                val existing = workInfos?.firstOrNull()
                Log.d("AAAAAAA", "66666")
//                if (existing == null || existing.state.isFinished) {
                    Log.d("AAAAAAA", "444444444")
                    scheduleTaskNotification(task, context)
//                }
            }
    }

    private fun scheduleTaskNotification(task: TodoResultItem, context: Context) {
        val initialDelay = task.startTimeMillis!! - System.currentTimeMillis()
        Log.d("AAAAAAA", "88888: "+initialDelay)
        Log.d("AAAAAAA", "8888811: "+task.startTimeMillis)
        Log.d("AAAAAAA", "8888822: "+System.currentTimeMillis())
        if (initialDelay != null) {
            if (initialDelay < 0) {
                Log.d("AAAAAAA", "7777777")
                return
            }
        }  // Skip past tasks

        val data = workDataOf(
            "taskId" to task.id,
            "taskTitle" to task.title
        )

        task.recurrenceIntervalMillis?.let {
            Log.d("AAAAAAA", "33333")

            if (initialDelay != null) {
                Log.d("AAAAAAA", "99999")

                val workRequest = PeriodicWorkRequestBuilder<TaskNotificationWorker>(
                    it, TimeUnit.MILLISECONDS
                )
                    .setInputData(data)
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    "TaskNotification_${task.id}",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            }
        }
    }
}