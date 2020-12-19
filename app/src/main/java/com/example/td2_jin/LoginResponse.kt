package com.example.td2_jin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
        @SerialName("token")
    val token : String
)
