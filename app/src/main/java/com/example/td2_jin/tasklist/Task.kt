package com.example.td2_jin.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
        @SerialName("id")
        val id : String,
        @SerialName("title")
        var title : String,
        @SerialName("description")
        var description: String = "default") : java.io.Serializable{

}