package com.android.androiddevelopertask.ui

import android.util.Log
import com.android.androiddevelopertask.dataClass.TodoDao
import com.android.androiddevelopertask.dataClass.TodoResult
import com.android.androiddevelopertask.dataClass.TodoResultItem
import com.android.androiddevelopertask.retrofit.ApiService
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService
//, private val dao: TodoDao
) {

//    val allTodos = dao.getAllNotes()

    suspend fun getTodos(): TodoResult? {
        return try {
            val response = apiService.getTodos()
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else if (response.code() == 400) {
                val responseErrorBody = response.errorBody()!!.string()
                val jsonObject = JSONObject(responseErrorBody)
                val message = jsonObject.getString("message")
                Log.d("getTodos", " response code = 400" + response.errorBody()!!.string())
                return TodoResult()
            } else if (response.code() == 403) {
                Log.d("getTodos", " response code = 403")
                return TodoResult()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("getTodos", " Exception" + e.toString())
            null
        }
    }

//    suspend fun insert(note: TodoResultItem){
//        dao.insert(note)
//    }
//
//    suspend fun delete(note: TodoResultItem){
//        dao.delete(note)
//    }
}