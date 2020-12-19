package com.example.td2_jin.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    var email: String,
    @SerialName("firstname")
    var firstName: String,
    @SerialName("lastname")
    var lastName: String,
    @SerialName("avatar")
    var avatar: String?
): java.io.Serializable{

}
