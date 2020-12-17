package com.example.td2_jin.userinfo

import android.widget.Toast
import androidx.lifecycle.*
import com.example.td2_jin.network.TasksRepository
import com.example.td2_jin.network.UserInfo
import com.example.td2_jin.network.UserInfoRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo : LiveData<UserInfo> = _userInfo

    fun loadUserInfo() {
        viewModelScope.launch {
            _userInfo.value = repository.loadUserInfo();
        }
    }

    fun updateUserInfo(userInfo: UserInfo, feedback: Toast){
        viewModelScope.launch {
            val updatedUserInfo = repository.updateUserInfo(userInfo)
            val editableUserInfo = _userInfo.value
            if(updatedUserInfo != null && editableUserInfo != null) {
                editableUserInfo.firstName = updatedUserInfo.firstName
                editableUserInfo.lastName = updatedUserInfo.lastName
                editableUserInfo.email = updatedUserInfo.email
                editableUserInfo.avatar = updatedUserInfo.avatar
                _userInfo.value = editableUserInfo!!
                feedback.setText("La modification de l'user à réussi")
            }else{
                feedback.setText("La modification de l'user à échoué, email invalide")
            }
            feedback.show();
        }
    }
}