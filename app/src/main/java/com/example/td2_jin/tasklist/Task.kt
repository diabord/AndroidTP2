package com.example.td2_jin.tasklist

import java.io.Serializable

data class Task(val id : String, val title : String, val description: String = "default") : Serializable {

}