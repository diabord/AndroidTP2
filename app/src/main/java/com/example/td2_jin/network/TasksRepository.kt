package com.example.td2_jin.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.td2_jin.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun addTask(task : Task) {
        val editedTask = tasksWebService.createTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.add(editedTask.body()!!)
        _taskList.value = editableList
    }

    suspend fun deleteTask(id : String) {
        val editedTask = tasksWebService.deleteTask(id)
        if(editedTask.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val deleteTask = editableList.find { id == it.id }
            editableList.remove(deleteTask)
            _taskList.value = editableList
        }
    }

    suspend fun updateTask(task : Task) {
        val editedTask = tasksWebService.updateTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = editedTask.body()!!
        _taskList.value = editableList
    }
}