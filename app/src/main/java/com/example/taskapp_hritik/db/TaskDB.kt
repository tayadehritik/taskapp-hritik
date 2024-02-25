package com.example.taskapp_hritik.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskapp_hritik.dao.TaskDao
import com.example.taskapp_hritik.models.Task

@Database(entities = [Task::class], version = 2)
abstract class TaskDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
