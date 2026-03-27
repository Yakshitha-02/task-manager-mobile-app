package com.example.taskmanager

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton

class TaskAdapter(
    private val list: MutableList<Task>
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.taskTitle)
        val editBtn: ImageButton = view.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = list[position]
        holder.title.text = task.title

        // DELETE
        holder.deleteBtn.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }

        // EDIT
        holder.editBtn.setOnClickListener {
            val context = holder.itemView.context
            val input = EditText(context)
            input.setText(task.title)

            AlertDialog.Builder(context)
                .setTitle("Edit Task")
                .setView(input)
                .setPositiveButton("Update") { _, _ ->
                    list[position] = Task(input.text.toString())
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}