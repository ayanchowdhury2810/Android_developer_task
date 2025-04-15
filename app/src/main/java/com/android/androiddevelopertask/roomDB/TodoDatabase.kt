package com.android.androiddevelopertask.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.androiddevelopertask.dataClass.TodoDao
import com.android.androiddevelopertask.dataClass.TodoResultItem

@Database(entities = [TodoResultItem::class], version = 2)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao
}