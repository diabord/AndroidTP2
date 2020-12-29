package com.example.td2_jin.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.td2_jin.R
import com.example.td2_jin.databinding.FragmentTaskListBinding
import com.example.td2_jin.network.Api
import com.example.td2_jin.network.UserInfo
import com.example.td2_jin.task.TaskActivity
import com.example.td2_jin.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.example.td2_jin.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.example.td2_jin.userinfo.UserInfoActivity
import com.example.td2_jin.userinfo.UserInfoActivity.Companion.EDIT_USER_INFO_CODE
import com.example.td2_jin.userinfo.UserInfoViewModel

@BindingAdapter("listItems")
fun setListItems(recyclerView: RecyclerView, tasklist : LiveData<List<Task>>?) {
    if(tasklist != null) {
        TaskListAdapter.INSTANCE.taskList = tasklist.value.orEmpty()
        TaskListAdapter.INSTANCE.notifyDataSetChanged()
    }
}

class TaskListFragment : Fragment() {
    private val taskListAdapter = TaskListAdapter.INSTANCE
    private val taskListViewModel: TaskListViewModel by viewModels() // On récupère une instance de ViewModel
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

       recyclerView.adapter = taskListAdapter

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        binding.buttonDeconnexion.setOnClickListener() {
            Api.INSTANCE.setToken("")
            findNavController().navigate(R.id.action_taskListFragment_to_authenticationFragment2)
        }

        userInfoViewModel.userInfo.observe(viewLifecycleOwner, {
            binding.userInfo = it
            val avatar = it.avatar
            if(avatar != null){
                binding.profilPicture.load(avatar) {
                    transformations(CircleCropTransformation())
                }
            }else{
                binding.profilPicture.load("https://toppng.com/public/uploads/thumbnail/an-error-occurred-john-cena-are-you-sure-about-that-11562978196xueu8aklz5.png") {
                    transformations(CircleCropTransformation())
                }
            }
        })

        taskListAdapter.onDeleteClickListener = {
            taskListViewModel.deleteTask(it)
        }

        taskListAdapter.onEditClickListener = {
            val intent = Intent(requireContext(), TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, it)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        binding.profilPicture.setOnClickListener{
            val userInfo : UserInfo = userInfoViewModel.userInfo.value!!
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra(UserInfoActivity.USERINFO_KEY, userInfo)
            startActivityForResult(intent, EDIT_USER_INFO_CODE)
            //startActivity(intent);
        }

        binding.taskListViewModel = taskListViewModel

        taskListViewModel.loadTasks();
        userInfoViewModel.loadUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == 0) return
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
                var feedback = Toast.makeText(context, null, Toast.LENGTH_LONG)
                userInfoViewModel.updateUserInfo(userInfo, feedback)
            }
        }
    }
}