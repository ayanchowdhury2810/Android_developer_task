package com.android.androiddevelopertask.dataClass

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: TodoResultItem)

    @Delete
    suspend fun delete(note: TodoResultItem)

    @Update
    suspend fun update(note: TodoResultItem)

    @Query("SELECT * FROM todos")
    fun getAllNotes(): Flow<List<TodoResultItem>>
}