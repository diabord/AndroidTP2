package com.example.td2_jin.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.td2_jin.tasklist.Task

class UserInfoRepository {
    private val userWebService = Api.userWebService

    suspend fun loadUserInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUserInfo(userInfo: UserInfo): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }
}