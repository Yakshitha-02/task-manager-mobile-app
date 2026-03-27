package com.example.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AddTaskDialogFragment(
    val onTaskAdded: (String) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_add_task_dialog, container, false)

        val editText = view.findViewById<EditText>(R.id.taskInput)
        val button = view.findViewById<Button>(R.id.addTaskBtn)

        button.setOnClickListener {
            val task = editText.text.toString()
            onTaskAdded(task)
            dismiss()
        }

        return view
    }
}