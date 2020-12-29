package com.example.td2_jin.network

import com.example.td2_jin.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.INSTANCE.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun addTask(task : Task) : Task? {
        val editedTask = tasksWebService.createTask(task)
        return if (editedTask.isSuccessful) editedTask.body() else null
    }

    suspend fun deleteTask(id : String) : Boolean {
        val editedTask = tasksWebService.deleteTask(id)
        return editedTask.isSuccessful
    }

    suspend fun updateTask(task : Task) : Task? {
        val editedTask = tasksWebService.updateTask(task)
        return if (editedTask.isSuccessful) editedTask.body() else null
    }
}