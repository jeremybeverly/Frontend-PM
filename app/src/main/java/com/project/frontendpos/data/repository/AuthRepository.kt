package com.project.frontendpos.data.repository

import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.LoginRequest
import com.project.frontendpos.data.remote.api.AuthService

class AuthRepository(
    private val authService: AuthService
) {

    suspend fun login(
        username: String,
        password: String
    ): Result<Unit> {

        return try {

            val response = authService.login(
                LoginRequest(username, password)
            )

            SessionManager.saveToken(response.token)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        SessionManager.clearSession()
    }

    fun isLoggedIn() =
        SessionManager.isLoggedIn()

}
