package com.example.td2_jin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginForm(
        @SerialName("email")
    var email : String = "",
        @SerialName("password")
    var password : String = ""
): java.io.Serializable{

}
