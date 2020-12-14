package com.example.td2_jin.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.td2_jin.R
import com.example.td2_jin.network.Api
import com.example.td2_jin.network.UserInfo
import com.example.td2_jin.task.TaskActivity
import com.example.td2_jin.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.example.td2_jin.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.example.td2_jin.userinfo.UserInfoActivity
import com.example.td2_jin.userinfo.UserInfoActivity.Companion.CHANGE_PROFILE_PICTURE_REQUEST_CODE
import com.example.td2_jin.userinfo.UserInfoActivity.Companion.EDIT_USER_INFO_CODE
import com.example.td2_jin.userinfo.UserInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
//import java.util.*


class TaskListFragment : Fragment() {
    /*private var _taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"))*/

    //private val tasksRepository = TasksRepository()

    private val taskListAdapter = TaskListAdapter()
    private val taskListViewModel: TaskListViewModel by viewModels() // On récupère une instance de ViewModel
    private val userInfoViewModel: UserInfoViewModel by viewModels()

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
        //val taskListAdapter = TaskListAdapter()
        /*taskListAdapter.onDeleteClickListener = {
            val pos = _taskList.indexOf(it)
            _taskList.remove(it)
            (recyclerView.adapter as TaskListAdapter).notifyItemRemoved(pos)

            lifecycleScope.launch {
                tasksRepository.deleteTask(it.id);
            }
        }

        taskListAdapter.onEditClickListener = {
            val intent = Intent(requireContext(), TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, it)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }*/
       recyclerView.adapter = taskListAdapter

        
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            //val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        /*tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskListAdapter.taskList.clear();
            taskListAdapter.taskList.addAll(it)
            taskListAdapter.notifyDataSetChanged()
        })*/

        taskListViewModel.taskList.observe(viewLifecycleOwner, Observer<List<Task>>{ tasks ->
            taskListAdapter.taskList = tasks.orEmpty()
            taskListAdapter.notifyDataSetChanged()
        })

        userInfoViewModel.userInfo.observe(viewLifecycleOwner, {
            val textView = view.findViewById<TextView>(R.id.networkTextView)
            textView?.text = "${it.firstName} ${it.lastName}"
        })

        taskListAdapter.onDeleteClickListener = {
            taskListViewModel.deleteTask(it)
        }

        taskListAdapter.onEditClickListener = {
            val intent = Intent(requireContext(), TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, it)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        val profilePicture = view?.findViewById<ImageView>(R.id.profilPicture)
        profilePicture.setOnClickListener{
            val userInfo : UserInfo = userInfoViewModel.userInfo.value!!
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra(UserInfoActivity.USERINFO_KEY, userInfo)
            startActivityForResult(intent, CHANGE_PROFILE_PICTURE_REQUEST_CODE)
        }

        taskListViewModel.loadTasks();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //if(requestCode == ) return
        when(requestCode){
            ADD_TASK_REQUEST_CODE -> {
                val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
                taskListViewModel.addTask(task)
            }
            EDIT_TASK_REQUEST_CODE -> {
                val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
                taskListViewModel.editTask(task);
            }
            EDIT_USER_INFO_CODE -> {
                val userInfo = data!!.getSerializableExtra(UserInfoActivity.USERINFO_KEY) as UserInfo
                userInfoViewModel.updateUserInfo(userInfo)
            }
        }


        /*val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task?
        if(task != null) {
            //val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
            if (requestCode == ADD_TASK_REQUEST_CODE) {
                taskListViewModel.addTask(task)
            } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
                taskListViewModel.editTask(task);
            }

            val userInfo = data!!.getSerializableExtra(UserInfoActivity.)

            super.onActivityResult(requestCode, resultCode, data)
        }*/
    }

    override fun onResume() {
        /*val textView = view?.findViewById<TextView>(R.id.networkTextView)
        val profilPicture = view?.findViewById<ImageView>(R.id.profilPicture)
        */
        /*lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            textView?.text = "${userInfo.firstName} ${userInfo.lastName}"
            if(userInfo.avatar != null){
                profilPicture?.load(userInfo.avatar) {
                    transformations(CircleCropTransformation())
                }
            }else{
                profilPicture?.load("https://toppng.com/public/uploads/thumbnail/an-error-occurred-john-cena-are-you-sure-about-that-11562978196xueu8aklz5.png") {
                    transformations(CircleCropTransformation())
                }
            }
        }*/

        userInfoViewModel.loadUserInfo()

        val profilPciture = view?.findViewById<ImageView>(R.id.profilPicture)
        profilPciture?.load("https://toppng.com/public/uploads/thumbnail/an-error-occurred-john-cena-are-you-sure-about-that-11562978196xueu8aklz5.png") {
            transformations(CircleCropTransformation())
        }


        /*lifecycleScope.launch {
            tasksRepository.refresh()
        }*/

        super.onResume()
    }
}