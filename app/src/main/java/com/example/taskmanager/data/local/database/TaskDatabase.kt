package com.example.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_db"
    }
}