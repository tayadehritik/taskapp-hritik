package com.example.taskapp_hritik.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskapp_hritik.models.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Insert
    fun insert(vararg task: Task)

    @Query("DELETE FROM Task")
    fun deleteAll()

    @Update
    fun updateTask(vararg task:Task)

}