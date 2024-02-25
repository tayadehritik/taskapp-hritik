package com.example.taskapp_hritik

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.taskapp_hritik.adapters.TaskListAdapter
import com.example.taskapp_hritik.databinding.ActivityMainBinding
import com.example.taskapp_hritik.db.TaskDB
import com.example.taskapp_hritik.models.Task
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launch {


        }

        CoroutineScope(Dispatchers.IO).launch {

            val db = (applicationContext as Application).db
            val taskDao = db.taskDao()
            var tasks = sortTasksByDate(taskDao.getAll(),false)
            println(tasks)
            withContext(Dispatchers.Main) {
                // Update UI here
                adapter = TaskListAdapter(tasks,taskDao)
                var recyclerView = binding.recyclerView
                recyclerView.adapter = adapter
            }
        }






        binding.dateEditText.setOnClickListener {
            binding.dateInputLayout.error = null
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            picker.show(supportFragmentManager,"test")

            picker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                picker.selection
                val timestamp: Long = it
                val date = Date(timestamp)
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("IST") // Set the timezone to IST
                val formattedDate = sdf.format(date)
                binding.dateEditText.setText(formattedDate)
                println(formattedDate)
            }
            picker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            picker.addOnCancelListener {
                // Respond to cancel button click.
            }
            picker.addOnDismissListener {
                // Respond to dismiss events.
            }
        }

        binding.addTaskButton.setOnClickListener {

            binding.dateInputLayout.error = null
            binding.taskInputLayout.error = null

            if(!isDateValid(binding.dateEditText.text.toString()))
            {
                binding.dateInputLayout.error = "Invalid date"
                return@setOnClickListener
            }

            if(binding.taskEditText.text.toString() == "" || binding.taskEditText.text!!.length > 100)
            {
                binding.taskInputLayout.error = "Invalid Task Name"
                return@setOnClickListener
            }


            var task = Task(0,
                binding.taskEditText.text.toString(),
                binding.dateEditText.text.toString(),
                binding.completedCheckbox.isChecked)

            CoroutineScope(Dispatchers.IO).launch {

                val db = (applicationContext as Application).db
                val taskDao = db.taskDao()
                taskDao.insert(task)
                adapter.dataSet = sortTasksByDate(db.taskDao().getAll(),false)

                withContext(Dispatchers.Main) {
                    // Update UI here
                    adapter.notifyDataSetChanged()
                }
            }

            println("here ${binding.completedCheckbox.isChecked}")

        }

        binding.deleteAllTasksButton.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val db = (applicationContext as Application).db
                val taskDao = db.taskDao()
                taskDao.deleteAll()
                adapter.dataSet = sortTasksByDate(db.taskDao().getAll(),false)

                withContext(Dispatchers.Main) {
                    // Update UI here
                    adapter.notifyDataSetChanged()
                }
            }

        }

        binding.sortByButton.setOnClickListener {
            val popup = PopupMenu(this,it)
            popup.menuInflater.inflate(R.menu.order_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.Date -> {
                        // Handle menu item 1 click
                        CoroutineScope(Dispatchers.IO).launch {

                            val db = (applicationContext as Application).db
                            val taskDao = db.taskDao()
                            adapter.dataSet = sortTasksByDate(db.taskDao().getAll(),true)

                            withContext(Dispatchers.Main) {
                                // Update UI here
                                adapter.notifyDataSetChanged()
                            }
                        }
                        true
                    }
                    R.id.Completed -> {
                        // Handle menu item 2 click
                        CoroutineScope(Dispatchers.IO).launch {

                            val db = (applicationContext as Application).db
                            val taskDao = db.taskDao()
                            adapter.dataSet = sortTasksByDate(db.taskDao().getAll(),false)

                            withContext(Dispatchers.Main) {
                                // Update UI here
                                adapter.notifyDataSetChanged()
                            }
                        }
                        true
                    }
                    // Handle other menu items if needed
                    else -> false
                }
            }

            popup.show()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Date -> {
                // Handle clicks on your menu item
                true
            }

            R.id.Completed -> {
                true
            }
            // Handle other menu items if needed
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun isDateValid(dateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        dateFormat.isLenient = false // Disable leniency to ensure strict date parsing

        return try {
            dateFormat.parse(dateStr) // Try to parse the date string
            true // If parsing succeeds, return true
        } catch (e: Exception) {
            false // If parsing fails, return false
        }
    }

    fun sortTasksByDate(tasks: List<Task>, flag:Boolean): List<Task>
    {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        if(flag) {
            return tasks.sortedBy { sdf.parse(it.date).time }
        }
        else
        {
            var sorted = tasks.sortedBy { sdf.parse(it.date).time }
            sorted = sorted.sortedBy { booleanToInt(it.completed) }.reversed()
            return sorted
        }
    }

    fun booleanToInt(value:Boolean): Int
    {
        if(value)
            return 1
        else
            return 0

    }


    fun editTask(task:Task)
    {

    }





}