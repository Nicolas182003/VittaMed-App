package com.example.vittamed.io.response

import com.example.vittamed.model.User

data class LoginResponse(
    val success: Boolean,
    val user: User,
    val jwt: String
)
