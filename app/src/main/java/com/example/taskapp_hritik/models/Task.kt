package com.example.taskapp_hritik.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "completed") var completed: Boolean
)
