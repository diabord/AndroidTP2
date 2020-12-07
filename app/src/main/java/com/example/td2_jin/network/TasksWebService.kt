package com.example.td2_jin.network

import com.example.td2_jin.tasklist.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks() : Response<List<Task>>
}