package com.example.td2_jin.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.td2_jin.R
import com.example.td2_jin.task.TaskActivity
import com.example.td2_jin.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class TaskListFragment : Fragment() {
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val taskListAdapter = TaskListAdapter(taskList)
        taskListAdapter.onDeleteClickListener = {
            task ->
            val pos = taskList.indexOf(task)
            taskList.remove(task)
            (recyclerView.adapter as TaskListAdapter).notifyItemRemoved(pos)
        }
        recyclerView.adapter = taskListAdapter

        
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            //val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        taskList.add(task)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        (recyclerView?.adapter as TaskListAdapter).notifyItemInserted(taskList.size - 1)

        super.onActivityResult(requestCode, resultCode, data)
    }
}