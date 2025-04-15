package com.android.androiddevelopertask.ui.screens.HomeScreen

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.androiddevelopertask.dataClass.TodoResult
import com.android.androiddevelopertask.dataClass.TodoResultItem
import com.android.androiddevelopertask.retrofit.Resource
import com.android.androiddevelopertask.ui.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val resources: Resources
): ViewModel() {

    private var _todoResult = MutableLiveData<Resource<TodoResult>>()
    val todoResult : LiveData<Resource<TodoResult>> = _todoResult

    private var _editData = MutableLiveData<TodoResultItem?>()
    val editData : LiveData<TodoResultItem?> = _editData

    val todos = repository.allTodos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTodoData(){
        viewModelScope.launch(Dispatchers.IO) {
            _todoResult.postValue(Resource.loading(null))
            val response = repository.getTodos()
            if(response != null){
                _todoResult.postValue(Resource.success(response))
            } else {
                _todoResult.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun getEditData(note: TodoResultItem){
        _editData.value = note
    }

    fun clearEditData(){
        _editData.value = null
    }

    fun addNote(content: String) {
        viewModelScope.launch {
            repository.insert(TodoResultItem(title = content, completed = false, userId = null))
        }
    }

    fun deleteNote(todo: TodoResultItem) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    fun updateNote(title: String, id: Int) {
        viewModelScope.launch {
            repository.update(TodoResultItem(title = title, completed = false, userId = null, id = id))
        }
    }

}