package com.project.frontendpos.data.remote

import com.project.frontendpos.data.model.LoginRequest
import com.project.frontendpos.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}