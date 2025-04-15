package com.android.androiddevelopertask.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

class TodoResult : ArrayList<TodoResultItem?>()

@Entity(tableName = "todos")
data class TodoResultItem(
    val completed: Boolean?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val title: String?,
    val userId: Int?
)