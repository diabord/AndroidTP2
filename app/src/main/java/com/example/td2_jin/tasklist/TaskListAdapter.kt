package com.example.td2_jin.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.td2_jin.databinding.ItemTaskBinding

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    companion object {
        lateinit var INSTANCE: TaskListAdapter
    }
    var taskList: List<Task> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListAdapter.TaskViewHolder {
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

    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onEditClickListener: ((Task) -> Unit)? = null
}