package com.project.frontendpos.data.remote

object SessionManager {
    var userToken: String? = null

    val formattedToken: String
        get() = "Bearer $userToken"

    fun clearSession() {
        userToken = null
    }
}