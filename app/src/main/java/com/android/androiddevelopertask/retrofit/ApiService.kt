package com.android.androiddevelopertask.retrofit

import com.android.androiddevelopertask.dataClass.TodoResult
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): Response<TodoResult>
}