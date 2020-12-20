package com.example.td2_jin.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.td2_jin.R
import com.example.td2_jin.databinding.ItemTaskBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    companion object {
        lateinit var INSTANCE: TaskListAdapter
    }
    var taskList: List<Task> = emptyList()

    private var _binding: ItemTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListAdapter.TaskViewHolder {
        /*val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskListAdapter.TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                binding.task = task

                binding.imageButton.setOnClickListener {
                    onDeleteClickListener?.invoke(task)
                }

                binding.editButton.setOnClickListener {
                    onEditClickListener?.invoke(task)
                }
            }
        }
    }

    /*inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                val textView =  findViewById<TextView>(R.id.task_title)
                textView.text = """${task.title}  |  ${task.description}"""

                findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
                    onDeleteClickListener?.invoke(task)
                }

                findViewById<ImageButton>(R.id.editButton).setOnClickListener {
                    onEditClickListener?.invoke(task)
                }
            }
        }
    }*/

    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onEditClickListener: ((Task) -> Unit)? = null
}