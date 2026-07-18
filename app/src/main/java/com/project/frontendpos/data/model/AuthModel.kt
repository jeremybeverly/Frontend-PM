package com.project.frontendpos.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserInfo? = null
)
