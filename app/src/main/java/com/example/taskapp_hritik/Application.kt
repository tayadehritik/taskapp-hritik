package com.example.taskapp_hritik
import android.app.Application
import android.content.res.Configuration
import androidx.room.Room.databaseBuilder
import com.example.taskapp_hritik.db.TaskDB


class Application: Application() {

    public lateinit var db:TaskDB

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        db = databaseBuilder(
            applicationContext,
            TaskDB::class.java, "TaskDB"
        ).build()
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged ( newConfig : Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }
}