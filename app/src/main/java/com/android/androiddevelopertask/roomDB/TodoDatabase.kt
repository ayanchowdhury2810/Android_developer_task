package com.android.androiddevelopertask.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.androiddevelopertask.dataClass.TodoDao
import com.android.androiddevelopertask.dataClass.TodoResultItem

@Database(entities = [TodoResultItem::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context) : TodoDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}