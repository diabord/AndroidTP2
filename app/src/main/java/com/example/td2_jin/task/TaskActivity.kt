package com.example.td2_jin.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.td2_jin.databinding.TaskInfoBinding
import com.example.td2_jin.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val EDIT_TASK_REQUEST_CODE = 420
        const val TASK_KEY = "TASK"
    }

    private var task : Task? = null

    private lateinit var binding: TaskInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TaskInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.task_info)

        task = intent.getSerializableExtra(TASK_KEY) as? Task
        if(task == null) {
            task = Task(id = UUID.randomUUID().toString(), title = "", description = "")
        }

        binding.task = task;

        binding.imageButtonValidate.setOnClickListener {
            if (TextUtils.isEmpty(task!!.title)) {
                binding.textTitle.error = "Ce champ doit être rempli"
            } else if (TextUtils.isEmpty(task!!.description)) {
                binding.textDescription.error = "Ce champ doit être rempli."
            } else {
                intent.putExtra(TASK_KEY, task);
                setResult(1, intent)
                finish()
            }
        }
    }
}