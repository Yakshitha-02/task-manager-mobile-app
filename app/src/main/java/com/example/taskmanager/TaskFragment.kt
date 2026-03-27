package com.example.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private val list = mutableListOf(
        Task("Study"),
        Task("Workout")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val addBtn = view.findViewById<Button>(R.id.addBtn)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(list)
        recyclerView.adapter = adapter

        addBtn.setOnClickListener {

            val dialog = AddTaskDialogFragment { taskText ->
                if (taskText.isNotEmpty()) {
                    list.add(Task(taskText))
                    adapter.notifyDataSetChanged()
                }
            }

            dialog.show(parentFragmentManager, "AddTaskDialog")
        }

        return view
    }
}