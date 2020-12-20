package com.example.td2_jin

import android.app.Application
import com.example.td2_jin.network.Api
import com.example.td2_jin.tasklist.TaskListAdapter

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
        TaskListAdapter.INSTANCE = TaskListAdapter()
    }
}