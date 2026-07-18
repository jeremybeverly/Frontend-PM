package com.project.frontendpos.data.remote

object SessionManager {
    var userToken: String? = null

    // Generates the proper "Bearer <token>" string required by Express/Passport middleware
    val formattedToken: String
        get() = "Bearer $userToken"

    fun clearSession() {
        userToken = null
    }
}