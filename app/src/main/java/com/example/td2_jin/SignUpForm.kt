package com.example.td2_jin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpForm(
        @SerialName("firstname")
        var firstname : String = "",
        @SerialName("lastname")
        var lastname : String = "",
        @SerialName("email")
        var email : String = "",
        @SerialName("password")
        var password : String = "",
        @SerialName("password_confirmation")
        var password_confirmation : String = "",
): java.io.Serializable{

}
