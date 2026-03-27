package com.example.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlinx.coroutines.*
import androidx.work.*
import java.util.concurrent.TimeUnit

class TaskFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private val list = mutableListOf<Task>()

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

        // 🔹 LOAD SAVED TASKS
        GlobalScope.launch(Dispatchers.Main) {
            val savedTasks = TaskDataStore.getTasks(requireContext())
            if (savedTasks.isNotEmpty()) {
                list.clear()
                list.addAll(savedTasks)
                adapter.notifyDataSetChanged()
            }
        }

        // ➕ ADD TASK
        addBtn.setOnClickListener {

            val dialog = AddTaskDialogFragment { taskText ->
                if (taskText.isNotEmpty()) {

                    val newTask = Task(0, 0, taskText, false)
                    list.add(newTask)
                    adapter.notifyDataSetChanged()

                    // save
                    GlobalScope.launch {
                        TaskDataStore.saveTasks(requireContext(), list)
                    }
                }
            }

            dialog.show(parentFragmentManager, "AddTaskDialog")
        }

        // 🌐 RETROFIT API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(
                call: Call<List<Task>>,
                response: Response<List<Task>>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    list.clear()
                    list.addAll(response.body()!!.take(10))
                    adapter.notifyDataSetChanged()

                    // save API data
                    GlobalScope.launch {
                        TaskDataStore.saveTasks(requireContext(), list)
                    }
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // ⏰ WORKMANAGER (background reminder)
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                "task_reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )

        return view
    }
}