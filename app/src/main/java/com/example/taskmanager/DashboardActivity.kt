package com.example.taskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TaskFragment())
            .commit()
    }
}