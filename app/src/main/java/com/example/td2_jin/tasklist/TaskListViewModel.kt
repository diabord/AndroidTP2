package com.example.td2_jin.tasklist

import androidx.lifecycle.*
import com.example.td2_jin.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    private val repository = TasksRepository()
    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            _taskList.value = repository.loadTasks();
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val newTask = repository.addTask(task);
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(newTask!!)
            _taskList.value = editableList
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val success = repository.deleteTask(task.id)
            if(success) {
                val editableList = _taskList.value.orEmpty().toMutableList()
                val deleteTask = editableList.find { task.id  == it.id }
                editableList.remove(deleteTask)
                _taskList.value = editableList
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = repository.updateTask(task)
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList[position] = updatedTask!!
            _taskList.value = editableList
        }
    }
}