package com.example.taskmanager

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "tasks")

object TaskDataStore {

    private val TASK_KEY = stringPreferencesKey("task_list")

    suspend fun saveTasks(context: Context, tasks: List<Task>) {
        val titles = tasks.joinToString(",") { it.title }

        context.dataStore.edit {
            it[TASK_KEY] = titles
        }
    }

    suspend fun getTasks(context: Context): List<Task> {
        val prefs = context.dataStore.data.first()
        val data = prefs[TASK_KEY] ?: ""

        if (data.isEmpty()) return emptyList()

        return data.split(",").map {
            Task(0, 0, it, false)
        }
    }
}