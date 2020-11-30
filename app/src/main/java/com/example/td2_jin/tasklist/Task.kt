package com.example.td2_jin.tasklist

import java.io.Serializable

data class Task(val id : String, var title : String, var description: String = "default") : Serializable {

}