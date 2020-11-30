package com.example.td2_jin.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.td2_jin.R
import com.example.td2_jin.tasklist.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val EDIT_TASK_REQUEST_CODE = 420
        const val TASK_KEY = "TASK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_info)

        var extra = intent.getSerializableExtra(TASK_KEY) as? Task

        if(extra != null){
            val title = findViewById<EditText>(R.id.textTitle)
            val desc = findViewById<EditText>(R.id.textDescription)
            title.setText(extra.title)
            desc.setText(extra.description)


        }

        findViewById<ImageButton>(R.id.imageButtonValidate).setOnClickListener {
            val title = findViewById<EditText>(R.id.textTitle)
            val desc = findViewById<EditText>(R.id.textDescription)
            if(extra == null){
                val newTask = Task(id = UUID.randomUUID().toString(), title = title.text.toString(), description = desc.text.toString())
                intent.putExtra(TASK_KEY, newTask)
            }
            else{
                extra.title = title.text.toString()
                extra.description = desc.text.toString()
            }
            setResult(1,intent)
            finish()
        }
    }
}