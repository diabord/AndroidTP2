package com.example.td2_jin.network

class UserInfoRepository {
    private val userWebService = Api.INSTANCE.userWebService

    suspend fun loadUserInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUserInfo(userInfo: UserInfo): UserInfo? {
        val response = userWebService.update(userInfo)
        return if (response.isSuccessful) response.body() else null
    }

    //info : plus utile
    /*suspend fun updateUserAvatar(userAvatar: String): String? {
        //val response = userWebService.updateAvatar()
        return if (response.isSuccessful) response.body() else null
    }*/
}