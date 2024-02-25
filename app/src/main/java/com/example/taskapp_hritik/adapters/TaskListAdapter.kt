package com.example.taskapp_hritik.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp_hritik.Application
import com.example.taskapp_hritik.R
import com.example.taskapp_hritik.dao.TaskDao
import com.example.taskapp_hritik.models.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskListAdapter(var inputDataSet: List<Task>, taskDao: TaskDao) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

        var dataSet = inputDataSet
        var taskDao = taskDao
        var isBeingEdited = false
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     *
     */



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val taskName: MaterialTextView
        val taskDate: MaterialTextView
        val completed: MaterialCheckBox
        val editButton: MaterialButton

        init {
            // Define click listener for the ViewHolder's View
            taskName = view.findViewById(R.id.taskName)
            taskDate = view.findViewById(R.id.taskDate)
            completed = view.findViewById(R.id.completed)
            editButton = view.findViewById(R.id.editButton)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)



        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.taskName.text = dataSet[position].name
        viewHolder.taskDate.text = dataSet[position].date
        viewHolder.completed.isChecked = dataSet[position].completed

        viewHolder.completed.setOnCheckedChangeListener { buttonView, isChecked ->
            dataSet[position].completed = isChecked
            CoroutineScope(Dispatchers.IO).launch {
                taskDao.updateTask(dataSet[position])
                withContext(Dispatchers.Main) {
                    // Update UI here
                    notifyDataSetChanged()
                }
            }
        }
        viewHolder.editButton.setOnClickListener {
            val dialogView = LayoutInflater.from(it.context).inflate(R.layout.custom_dialog, null)
            var editTaskText = dialogView.findViewById<EditText>(R.id.editTaskText)
            editTaskText.setText(dataSet[position].name)
            val abc = MaterialAlertDialogBuilder(it.context)
                .setTitle("Edit Task Title")
                .setView(dialogView)
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to negative button press
                    val editTaskText = dialogView.findViewById<EditText>(R.id.editTaskText)

                }
                .setPositiveButton("Save") { dialog, which ->
                    // Respond to positive button press

                    dataSet[position].name =  editTaskText.text.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        taskDao.updateTask(dataSet[position])
                        withContext(Dispatchers.Main) {
                            // Update UI here
                            notifyDataSetChanged()
                        }
                    }

                }
                .show()
            abc.findViewById<EditText>(R.id.editTaskText)
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}
